package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;
@Service("GitHubService")
//@RequiredArgsConstructor
public class GitHubClient {
    @Autowired
    @Qualifier("gitHubClient")
    private final WebClient gitHubClient;

    public GitHubClient(WebClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }


    public GitHubResponse fetchRepository(String user, String repository) {
        return gitHubClient
                .get()
                .uri("/repos/{user}/{repository}", user, repository)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .block();
    }
}