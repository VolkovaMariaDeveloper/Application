package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

public class GitHubClient {


    final private WebClient webClient;

    public GitHubClient(WebClient gitHubClient) {
        this.webClient = gitHubClient;
    }

    public GitHubResponse fetchRepository(String user, String repository) {
        return webClient
                .get()
                .uri("/repos/{user}/{repository}", user, repository)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .block();
    }
}