package ru.tinkoff.edu.java.scrapper.service.sender;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface UpdateMessageSender {
    void send(LinkUpdateRequest update);
}
