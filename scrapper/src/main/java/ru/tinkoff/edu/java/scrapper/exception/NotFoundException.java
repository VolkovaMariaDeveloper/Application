package ru.tinkoff.edu.java.scrapper.exception;

import ru.tinkoff.edu.java.scrapper.dto.response.SimpleLinkResponse;

public class NotFoundException extends RuntimeException {
    private final SimpleLinkResponse link;

    public NotFoundException(SimpleLinkResponse link) {
        this.link = link;
        System.out.println(String.format("Ссылка %s не найдена", link.url()));
    }
}
