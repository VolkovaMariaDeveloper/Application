package ru.tinkoff.edu.java.scrapper.dto.response;

import java.time.OffsetDateTime;

public record JooqLinkResponse(String url, long id, OffsetDateTime lastCheckTime, int count) {
}
