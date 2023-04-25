package ru.tinkoff.edu.java.scrapper.dto.response;

import java.util.List;

public record ListLinksResponse(List<JdbcLinkResponse> links) {
}
