package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;
@Service("StackOverflowService")
@RequiredArgsConstructor
public class StackOverflowClient {
    @Autowired
    @Qualifier("stackOverflowClient")
     private final WebClient stackOverflowClient;


    public StackOverflowResponse fetchQuestion(long idQuestion) {
        return stackOverflowClient.get()
                .uri("/questions/{ids}", idQuestion)
                .retrieve()
                .bodyToMono(StackOverflowResponse.class)
                .block();
    }
}
