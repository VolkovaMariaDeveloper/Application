package ru.tinkoff.edu.java.scrapper.dto.response;

import java.time.OffsetDateTime;

public record LinkResponse(long id, String description, String url, OffsetDateTime lastCheckTime,
                           int count) {//, List<String> branches) {

}
