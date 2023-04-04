package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record GitHubResponse(String name, OffsetDateTime updatedAt) {
}
