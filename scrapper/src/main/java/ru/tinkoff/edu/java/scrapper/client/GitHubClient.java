package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

@RequiredArgsConstructor
public class GitHubClient {

    private final WebClient gitHubClient;



    public GitHubResponse fetchRepository(String user, String repository) {
        return gitHubClient
                .get()
                .uri("/repos/{user}/{repository}", user, repository)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .block();
    }
}