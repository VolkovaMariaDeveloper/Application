package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

import java.util.List;
//@Service//("BotService")
//@RequiredArgsConstructor
public class BotClient {
    @Qualifier("botClient")
    @Autowired
    private final WebClient botWebClient;

    public BotClient(WebClient botWebClient) {
        this.botWebClient = botWebClient;
    }

    public void updateLink(Long id, String url, String description, List<Long> tgChatIds) {
        botWebClient.post()
                .uri("/updates")
                .bodyValue(new LinkUpdateRequest(id, url, description, tgChatIds))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
