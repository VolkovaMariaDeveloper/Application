package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;

public class ClientConfiguration {
    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private static final String STACK_OVERFLOW_BASE_URL = "https://api.stackexchange.com";


    @Bean("GitHubClient")
    public WebClient gitHubClient() {
        return WebClient.builder()
                .baseUrl(GITHUB_BASE_URL)
                .build();
    }

    @Bean("GitHubClient")
    public WebClient gitHubClient(String URL) {
        return WebClient.builder()
                .baseUrl(URL)
                .build();
    }

    @Bean("GitHubClient")
    public GitHubClient githubClient(WebClient gitHubClient) {
        return new GitHubClient(gitHubClient);
    }

    @Bean("StackOverflowClient")
    public WebClient stackOverflowClient() {
        return WebClient.builder()
                .baseUrl(STACK_OVERFLOW_BASE_URL)
                .build();
    }

    @Bean("StackOverflowClient")
    public WebClient stackOverflowClient(String URL) {
        return WebClient.builder()
                .baseUrl(URL)
                .build();
    }

    @Bean("StackOverflowClient")
    public StackOverflowClient stackOverflowClient(WebClient stackOverflowClient) {
        return new StackOverflowClient(stackOverflowClient);
    }
}
