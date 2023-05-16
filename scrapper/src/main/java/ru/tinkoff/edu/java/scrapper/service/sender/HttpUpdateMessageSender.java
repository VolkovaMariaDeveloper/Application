package ru.tinkoff.edu.java.scrapper.service.sender;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@RequiredArgsConstructor
public class HttpUpdateMessageSender implements UpdateMessageSender {

    private final BotClient botClient;

    @Override
    public void send(LinkUpdateRequest update) {
        botClient.updateLink(update);
    }
}
