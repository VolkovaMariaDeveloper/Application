package ru.tinkoff.edu.java.bot.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

import java.util.Collections;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class ScrapperClient {
    private final WebClient webClient;
    private static final String CHAT_ID_HEADER = "Tg-Chat-Id";

    public boolean registerChat(String chatId) {
        return Boolean.TRUE.equals(webClient.post()
                .uri("/tg-chat/{chatId}", chatId)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(throwable -> {
                    log.error("Chat registration error", throwable);
                    return Mono.just(false);
                })
                .block());
    }

    public boolean deleteChat(String chatId) {
        return Boolean.TRUE.equals(webClient.delete()
                .uri("/tg-chat/{chatId}", chatId)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(throwable -> {
                    log.error("Error while deleting chat", throwable);
                    return Mono.just(false);
                })
                .block());
    }
    public ListLinkResponse getTrackedLinks(long chatId) {
        return webClient.get()
                .uri("/links")
                .header(CHAT_ID_HEADER, String.valueOf(chatId))
                .retrieve()
                .bodyToMono(ListLinkResponse.class)
                .onErrorResume(throwable -> {
                    log.error("Error while getting tracked links", throwable);
                    return Mono.just(new ListLinkResponse(Collections.emptyList(), 0));
                })
                .block();
    }
    public boolean addLinkToTrack(String chatId, String link) {
        return Boolean.TRUE.equals(webClient.post()
                .uri("/links")
                .header(CHAT_ID_HEADER, chatId)
                .bodyValue(Map.of("link", link))
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(throwable -> {
                    log.error("Error while adding link", throwable);
                    return Mono.just(false);
                })
                .block());
    }
    public boolean removeLinkFromTrack(String chatId, String link) {
        return Boolean.TRUE.equals(webClient.delete()
                .uri("/links")
                .header(CHAT_ID_HEADER, chatId)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(throwable -> {
                    log.error("Error while removing link", throwable);
                    return Mono.just(false);
                })
                .block());
    }

}
