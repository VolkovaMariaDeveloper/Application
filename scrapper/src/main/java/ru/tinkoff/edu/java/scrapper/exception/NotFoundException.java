package ru.tinkoff.edu.java.scrapper.exception;

import lombok.extern.log4j.Log4j2;
import ru.tinkoff.edu.java.scrapper.dto.response.SimpleLinkResponse;

@Log4j2
public class NotFoundException extends RuntimeException {
    private final SimpleLinkResponse link;

    public NotFoundException(SimpleLinkResponse link) {
        this.link = link;
        log.info("Ссылка {} не найдена", link.url());
    }
}
