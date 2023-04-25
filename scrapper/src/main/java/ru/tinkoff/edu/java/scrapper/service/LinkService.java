package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.response.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;

import java.util.Collection;

public interface LinkService {
    JdbcLinkResponse add(long tgChatId, String url);
    JdbcLinkResponse remove(long tgChatId, String url);
    ListLinksResponse listAll(long tgChatId);
    Collection<JdbcLinkResponse> getAllLinks();
    Collection<JdbcLinkResponse> getAllUncheckedLinks();


}
