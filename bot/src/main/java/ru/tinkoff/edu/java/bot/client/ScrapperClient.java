package ru.tinkoff.edu.java.bot.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

import java.util.Collections;
import java.util.Map;

@Log4j2
@Component
public class ScrapperClient {

    private static final String CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String URI_LINKS = "/links";
    private static final String URI = "/tg-chat/{chatId}";
    private static final String ERROR_MESSAGE = "Error while getting tracked links";
    private static final String LINK = "link";
    private final WebClient scrapperWebClient;

    @Autowired
    public ScrapperClient(WebClient scrapperWebClient) {
        this.scrapperWebClient = scrapperWebClient;
    }

    public boolean registerChat(String chatId) {
        return Boolean.TRUE.equals(scrapperWebClient.post()
            .uri(URI, chatId)
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
        return Boolean.TRUE.equals(scrapperWebClient.method(HttpMethod.DELETE)
            .uri(URI, chatId)
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
        return scrapperWebClient.get()
            .uri(URI_LINKS)
            .header(CHAT_ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinkResponse.class)
            .onErrorResume(throwable -> {
                log.error(ERROR_MESSAGE, throwable);
                return Mono.just(new ListLinkResponse(Collections.emptyList()));
            })
            .block();
    }

    public boolean addLinkToTrack(String chatId, String link) {
        return Boolean.TRUE.equals(scrapperWebClient.post()
            .uri(URI_LINKS)
            .header(CHAT_ID_HEADER, chatId)
            .bodyValue(Map.of(LINK, link))
            .retrieve()
            .toBodilessEntity()
            .map(response -> response.getStatusCode().is2xxSuccessful())
            .onErrorResume(throwable -> {
                log.error("Error while adding link", throwable);
                return Mono.just(false);
            })
            .block());
    }

    public LinkResponse removeLinkFromTrack(String chatId, String link) {
        return scrapperWebClient.method(HttpMethod.DELETE)
            .uri(URI_LINKS)
            .header(CHAT_ID_HEADER, chatId)
            .bodyValue(Map.of(LINK, link))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(throwable -> {
                log.error(ERROR_MESSAGE, throwable);
                return Mono.just(null);
            })
            .block();
    }

}
