package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;

@RequiredArgsConstructor
public class StackOverflowClient {
     private final WebClient stackOverflowClient;


    public StackOverflowResponse fetchQuestion(long idQuestion) {
        return stackOverflowClient.get()
                .uri("/questions/{ids}", idQuestion)
                .retrieve()
                .bodyToMono(StackOverflowResponse.class)
                .block();
    }
}
