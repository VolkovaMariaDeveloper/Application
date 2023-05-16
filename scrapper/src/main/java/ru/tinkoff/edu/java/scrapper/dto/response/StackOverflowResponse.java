package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public record StackOverflowResponse(StackOverflowResponseItem[] items) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record StackOverflowResponseItem(Integer answerCount) {
    }
}
