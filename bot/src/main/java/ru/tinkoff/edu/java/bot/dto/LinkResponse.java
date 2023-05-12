package ru.tinkoff.edu.java.bot.dto;

import java.time.OffsetDateTime;

public record LinkResponse(long id, String description, String url, OffsetDateTime lastCheckTime, int count) {
}
