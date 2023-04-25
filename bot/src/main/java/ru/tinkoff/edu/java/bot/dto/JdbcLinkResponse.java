package ru.tinkoff.edu.java.bot.dto;

import java.time.OffsetDateTime;

public record JdbcLinkResponse(long id, String description, String link, OffsetDateTime lastCheckTime) {
}
