package ru.tinkoff.edu.java.scrapper.service.mappers;

import ru.tinkoff.edu.java.scrapper.dto.response.JooqLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;

import java.util.ArrayList;
import java.util.List;

public final class JooqMapper {
    private JooqMapper() {
    }

    public static LinkResponse map(JooqLinkResponse link) {
        return new LinkResponse(link.id(), null, link.url(), link.lastCheckTime(), link.count());
    }

    public static ListLinksResponse mapList(List<JooqLinkResponse> links) {
        List<LinkResponse> result = new ArrayList<>();
        for (JooqLinkResponse link : links) {
            result.add(map(link));
        }
        return new ListLinksResponse(result);
    }
}
