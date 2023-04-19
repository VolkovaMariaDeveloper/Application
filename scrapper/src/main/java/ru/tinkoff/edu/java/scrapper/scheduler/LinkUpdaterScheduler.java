package ru.tinkoff.edu.java.scrapper.scheduler;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkService linkService;
    private final TgChatService tgChatService;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final  BotClient botClient;
    private List<Long> tgChatIds;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        Collection<JdbcLinkResponse> listLinks = linkService.getAllUncheckedLinks();
        Collection<JdbcLinkResponse> updatedLinks = new HashSet<>();
        for (JdbcLinkResponse link : listLinks) {
            String url = link.link();
            ParserResult result = LinkParser.parseLink(url);
            if (result instanceof GitHubParserResult) {
                Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
                String user = pair.getKey();
                String repo = pair.getValue();
                GitHubResponse githubRepoResponse = gitHubClient.fetchRepository(user, repo);
                if (githubRepoResponse.updatedAt().isAfter(link.lastCheckTime())) {
                    updatedLinks.add(link);
                }
            } else if (result instanceof StackOverflowParserResult) {
                String questionId = ((StackOverflowParserResult) result).idQuestion;
                long id = Long.parseLong(questionId);
                StackOverflowResponse stackOverflowResponse = stackOverflowClient.fetchQuestion(id);
                if (stackOverflowResponse.updatedAt().isAfter(link.lastCheckTime())) {
                    updatedLinks.add(link);
                }
            } else
                log.warn("Link {} is not supported", url);
        }
        for (JdbcLinkResponse link : updatedLinks) {
            String description = String.format("%s has a new update!", link.link());
            tgChatIds = tgChatService.getChatIdsForLink(link.link());
            botClient.updateLink(link.id(), description, link.link(), tgChatIds);
        }
    }
}
