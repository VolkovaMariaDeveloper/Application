package ru.tinkoff.edu.java.scrapper.scheduler;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@EnableScheduling
@RequiredArgsConstructor
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
    private final BotClient botClient;
    private List<Long> tgChatIds;
    private final String BRANCH_ADDED = "Была добавлена новая ветка";
    private final String BRANCH_REMOVED = "Количество веток уменьшилось";

    private final String ANSWER_ADDED = "Появился новый ответ";
    private final String description = "Ссылка %s обновилась: %s";

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        ListLinksResponse listLinks = linkService.getAllUncheckedLinks();
        List<LinkUpdateRequest> listUpdater = new ArrayList<>();

        for (LinkResponse link : listLinks.links()) {
            String url = link.link();
            ParserResult result = LinkParser.parseLink(url);
            if (result instanceof GitHubParserResult) {
                Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
                String user = pair.getKey();
                String repo = pair.getValue();
                int countBranches = gitHubClient.fetchRepository(user, repo).size();
                if (countBranches > link.count()) {
                    tgChatIds = tgChatService.getChatIdsForLink(url);
                    listUpdater.add(new LinkUpdateRequest(link.id(), url, String.format(description, url, BRANCH_ADDED),tgChatIds,countBranches));
                    log.info("Link {} has a new update", url);
                }
                if (countBranches < link.count()) {
                    tgChatIds = tgChatService.getChatIdsForLink(url);
                    listUpdater.add(new LinkUpdateRequest(link.id(), url, String.format(description, url, BRANCH_REMOVED),tgChatIds,countBranches));
                    log.info("Link {} has a new update", url);
                }

            } else if (result instanceof StackOverflowParserResult) {
                String questionId = ((StackOverflowParserResult) result).idQuestion;
                long id = Long.parseLong(questionId);
                StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
                int countAnswers = list[0].answer_count();
                if (countAnswers > link.count()){
                    tgChatIds = tgChatService.getChatIdsForLink(url);
                    listUpdater.add(new LinkUpdateRequest(link.id(), url, String.format(description, url, ANSWER_ADDED),tgChatIds,countAnswers));
                }

            } else
                log.warn("Link {} is not supported", url);
        }
        for (LinkUpdateRequest link : listUpdater) {
            log.info("Link {} has a new update", link.url());
            botClient.updateLink(link);
            log.info("Link {} has a new update, we inform the following chats: {} ", link.url(), tgChatIds);
            linkUpdater.update(link.count(),link.url());
        }
    }
}
