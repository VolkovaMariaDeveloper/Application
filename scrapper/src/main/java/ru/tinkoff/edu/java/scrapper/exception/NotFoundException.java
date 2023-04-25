package ru.tinkoff.edu.java.scrapper.exception;

import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

public class NotFoundException extends RuntimeException{
    private final LinkResponse link;

    public NotFoundException(LinkResponse link){
        this.link = link;
        System.out.println(String.format("Ссылка %s не найдена",link.url()));
    }
}
