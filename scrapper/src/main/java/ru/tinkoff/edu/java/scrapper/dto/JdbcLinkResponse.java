package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record JdbcLinkResponse(long id, String description, String link, OffsetDateTime lastCheckTime) {
}
