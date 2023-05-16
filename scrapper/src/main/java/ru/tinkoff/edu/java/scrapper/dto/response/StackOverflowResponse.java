package ru.tinkoff.edu.java.scrapper.dto.response;

public record StackOverflowResponse(StackOverflowResponseItem[] items) {

    public record StackOverflowResponseItem(Integer answer_count) {
    }
}
