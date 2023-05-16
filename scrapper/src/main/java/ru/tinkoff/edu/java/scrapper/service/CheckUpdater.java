package ru.tinkoff.edu.java.scrapper.service;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;

@Component
@RequiredArgsConstructor
public class CheckUpdater {
    @Autowired
    private final GitHubClient gitHubClient;
    @Autowired
    private final StackOverflowClient stackOverflowClient;

    public int fillCount(String url) {
        ParserResult result = LinkParser.parseLink(url);
        if (result instanceof GitHubParserResult) {
            Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
            String user = pair.getKey();
            String repo = pair.getValue();
            return gitHubClient.fetchRepository(user, repo).size();
        } else if (result instanceof StackOverflowParserResult) {
            String questionId = ((StackOverflowParserResult) result).idQuestion;
            long id = Long.parseLong(questionId);
            StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
            return list[0].answerCount();
        } else {
            return -1;
        }
    }
}
