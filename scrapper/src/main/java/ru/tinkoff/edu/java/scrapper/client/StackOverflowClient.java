package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    final private WebClient webClient;

    public StackOverflowClient(WebClient stackOverflowClient) {

        this.webClient = stackOverflowClient;
    }

    public StackOverflowClient fetchQuestion(long idQuestion) {
        return webClient.get()
                .uri("/questions/{ids}", idQuestion)
                .retrieve()
                .bodyToMono(StackOverflowClient.class)
                .block();
    }
}
