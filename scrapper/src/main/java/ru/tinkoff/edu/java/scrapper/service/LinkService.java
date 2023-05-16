package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;

public interface LinkService {
    LinkResponse add(long tgChatId, String url);

    LinkResponse remove(long tgChatId, String url);

    ListLinksResponse findAllByChatId(long tgChatId);

    ListLinksResponse getAllLinks();

    ListLinksResponse getAllUncheckedLinks();

}
