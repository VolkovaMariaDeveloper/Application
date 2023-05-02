package ru.tinkoff.edu.java.scrapper.dto.response;

import java.time.OffsetDateTime;

public record LinkResponse(long id, String description, String link, OffsetDateTime lastCheckTime, int count){//, List<String> branches) {
}
