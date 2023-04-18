package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;

public interface LinkService {
    JdbcLinkResponse add(long tgChatId, String url);
    JdbcLinkResponse remove(long tgChatId, String url);
    ListLinksResponse listAll(long tgChatId);
    ListLinksResponse getAllLinks();

}
