package ru.tinkoff.edu.java.bot.dto;

import java.time.OffsetDateTime;

public record LinkResponse(long id, String description, String link, OffsetDateTime lastCheckTime) {
}
