package ru.tinkoff.edu.java.scrapper.scheduler;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateMessageSender;

@Log4j2
@Component
@EnableScheduling
@RequiredArgsConstructor
@ComponentScan(basePackages = {"ru.tinkoff.edu.java.scrapper.configuration"})
public class LinkUpdaterScheduler {
    @Autowired
    private final LinkService linkService;
    @Autowired
    private final LinkUpdater linkUpdater;
    @Autowired
    private final TgChatService tgChatService;
    @Autowired
    private final GitHubClient gitHubClient;
    @Autowired
    private final StackOverflowClient stackOverflowClient;

    @Autowired
    private final UpdateMessageSender updateMessageSender;
    private static final String BRANCH_ADDED = "Была добавлена новая ветка";
    private static final String BRANCH_REMOVED = "Количество веток уменьшилось";
    private static final String ANSWER_ADDED = "Появился новый ответ";
    private static final String DESCRIPTION = "Ссылка %s обновилась: %s";
    private static final String LOG_MESSAGE = "Link {} has a new update";
    private List<Long> tgChatIds;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        ListLinksResponse listLinks = linkService.getAllUncheckedLinks();
        List<LinkUpdateRequest> listUpdater = new ArrayList<>();
        for (LinkResponse link : listLinks.links()) {
            String url = link.url();
            ParserResult result = LinkParser.parseLink(url);
            if (result instanceof GitHubParserResult) {
                Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
                String user = pair.getKey();
                String repo = pair.getValue();
                int countBranches = gitHubClient.fetchRepository(user, repo).size();
                if (countBranches > link.count()) {
                    tgChatIds = tgChatService.getAllChatByLink(url);
                    listUpdater.add(new LinkUpdateRequest(
                        link.id(),
                        url,
                        String.format(DESCRIPTION, url, BRANCH_ADDED),
                        tgChatIds,
                        countBranches
                    ));
                    log.info(LOG_MESSAGE, url);
                }
                if (countBranches < link.count()) {
                    tgChatIds = tgChatService.getAllChatByLink(url);
                    listUpdater.add(new LinkUpdateRequest(
                        link.id(),
                        url,
                        String.format(DESCRIPTION, url, BRANCH_REMOVED),
                        tgChatIds,
                        countBranches
                    ));
                    log.info(LOG_MESSAGE, url);
                }

            } else if (result instanceof StackOverflowParserResult) {
                String questionId = ((StackOverflowParserResult) result).idQuestion;
                long id = Long.parseLong(questionId);
                StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
                int countAnswers = list[0].answer_count();
                if (countAnswers > link.count()) {
                    tgChatIds = tgChatService.getAllChatByLink(url);
                    listUpdater.add(new LinkUpdateRequest(
                        link.id(),
                        url,
                        String.format(DESCRIPTION, url, ANSWER_ADDED),
                        tgChatIds,
                        countAnswers
                    ));
                }

            } else {
                log.warn("Link {} is not supported", url);
            }
        }
        for (LinkUpdateRequest link : listUpdater) {
            log.info(LOG_MESSAGE, link.url());
            updateMessageSender.send(link);
            log.info("Link {} has a new update, we inform the following chats: {} ", link.url(), tgChatIds);
            linkUpdater.update(link.count(), link.url());
        }
    }
}
