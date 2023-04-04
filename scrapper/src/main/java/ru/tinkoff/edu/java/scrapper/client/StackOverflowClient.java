package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class StackOverflowClient {
     private final WebClient stackOverflowClient;


    public StackOverflowClient fetchQuestion(long idQuestion) {
        return stackOverflowClient.get()
                .uri("/questions/{ids}", idQuestion)
                .retrieve()
                .bodyToMono(StackOverflowClient.class)
                .block();
    }
}
