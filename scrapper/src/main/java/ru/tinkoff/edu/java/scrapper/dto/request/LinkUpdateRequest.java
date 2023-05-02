package ru.tinkoff.edu.java.scrapper.dto.request;

import java.util.List;

public record LinkUpdateRequest(Long id, String url, String description, List<Long> tgChatId, int count) {
}
