package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;

@Service("StackOverflowService")
@RequiredArgsConstructor
public class StackOverflowClient {
    @Autowired
    @Qualifier("stackOverflowWebClient")
    private final WebClient stackOverflowClient;

    public StackOverflowResponse fetchQuestion(long idQuestion) {
        return stackOverflowClient.get()
            .uri(uri -> uri.path("/questions/{id}")
                .queryParam("site", "stackoverflow")
                .build(idQuestion))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }

}
