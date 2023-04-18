package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import java.util.List;

@RequiredArgsConstructor
public class BotClient {
    private final WebClient botWebClient;

    public void updateLink(Long id, String url, String description, List<Long> tgChatIds) {
        botWebClient.post()
                .uri("/updates")
                .bodyValue(new LinkUpdateRequest(id, url, description, tgChatIds))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
