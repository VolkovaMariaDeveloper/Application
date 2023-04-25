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
import ru.tinkoff.edu.java.scrapper.dto.response.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    @Autowired
    private final LinkService linkService;
    @Autowired
    private final TgChatService tgChatService;
    @Autowired
    // @Qualifier("GitHubService")
    private final GitHubClient gitHubClient;

    @Autowired
    //@Qualifier("StackOverflowService")
    private final StackOverflowClient stackOverflowClient;
    @Autowired
    // @Qualifier("BotService")
    private final BotClient botClient;
    private List<Long> tgChatIds;
    private final String BRANCH_ADDED = "была добавлена новая ветка";
    private final String BRANCH_REMOVED = "количество веток уменьшилось";

    private final String ANSWER_ADDED = "появился новый ответ";

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        Collection<JdbcLinkResponse> listLinks = linkService.getAllUncheckedLinks();
        HashMap<JdbcLinkResponse, String> updatedLinks = new HashMap<JdbcLinkResponse, String>();

        for (JdbcLinkResponse link : listLinks) {
            String url = link.link();
            ParserResult result = LinkParser.parseLink(url);
            if (result instanceof GitHubParserResult) {
                Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
                String user = pair.getKey();
                String repo = pair.getValue();
                int countBranches = gitHubClient.fetchRepository(user, repo).size();
                if (countBranches > link.count()) {
                    updatedLinks.put(link, BRANCH_ADDED);
                    log.info("Link {} has a new update", url);
                }
                if (countBranches < link.count()) {
                    updatedLinks.put(link, BRANCH_REMOVED);
                    log.info("Link {} has a new update", url);
                }

            } else if (result instanceof StackOverflowParserResult) {
                String questionId = ((StackOverflowParserResult) result).idQuestion;
                long id = Long.parseLong(questionId);
                StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
                int countAnswers = list[0].answer_count();
                if (countAnswers > link.count()){
                    updatedLinks.put(link, ANSWER_ADDED);
                }

            } else
                log.warn("Link {} is not supported", url);
        }
        for (JdbcLinkResponse link : updatedLinks.keySet()) {
            log.info("Link {} has a new update", link.link());
            String description = String.format("Ссылка %s обновилась: %s", link.link(), updatedLinks.get(link));
            tgChatIds = tgChatService.getChatIdsForLink(link.link());
            botClient.updateLink(new LinkUpdateRequest(link.id(), link.link(),description, tgChatIds));
            log.info("Link {} has a new update, we inform the following chats: {} ", link.link(), tgChatIds);
        }
    }
}
