package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;

import java.util.Collection;

public interface LinkService {
    LinkResponse add(long tgChatId, String url);
    LinkResponse remove(long tgChatId, String url);
    ListLinksResponse listAll(long tgChatId);
    Collection<LinkResponse> getAllLinks();
    Collection<LinkResponse> getAllUncheckedLinks();


}
