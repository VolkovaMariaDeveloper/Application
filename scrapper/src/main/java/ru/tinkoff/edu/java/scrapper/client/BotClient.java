package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@Service("BotService")
@RequiredArgsConstructor
public class BotClient {
    @Autowired
    @Qualifier("botWebClient")
    private final WebClient botWebClient;

    public void updateLink(LinkUpdateRequest linkUpdateRequest) {
        botWebClient.post()
                .uri("/updates")
                .body(BodyInserters.fromValue(linkUpdateRequest))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
