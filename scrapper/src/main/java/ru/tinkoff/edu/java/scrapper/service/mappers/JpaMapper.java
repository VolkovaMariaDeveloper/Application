package ru.tinkoff.edu.java.scrapper.service.mappers;

import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entity.Links;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaMapper {
    public static LinkResponse map(Links link) {
        long id = link.getId();
        String url = link.getUrl();
        OffsetDateTime time = link.getLastCheckTime();
        int count = link.getCount();
        return new LinkResponse(id, null, url, time, count);
    }

    public static ListLinksResponse mapList(List<Links> links){
        List<LinkResponse> result = new ArrayList<>();
        for(Links link: links){
            result.add(map(link));
        }
        return new ListLinksResponse(result);
    }
}
