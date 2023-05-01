package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.OffsetDateTime;
import java.util.List;


@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    @Autowired
    private CheckUpdater checkUpdater;
    @Autowired
    JooqLinkRepository jooqLinkRepository;
    @Override
    public LinkResponse add(long tgChatId, String url) {
        int count = checkUpdater.fillCount(url);
        long id = jooqLinkRepository.add(tgChatId, url, count);
        return new LinkResponse(id, null, url, OffsetDateTime.now(),count);
    }

    @Override
    public LinkResponse remove(long tgChatId, String url) {
        int count = checkUpdater.fillCount(url);
        long id = jooqLinkRepository.remove(tgChatId, url);

        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public ListLinksResponse findAllByChatId(long tgChatId) {
        List<LinkResponse> collection = jooqLinkRepository.findAll(tgChatId);
        return new ListLinksResponse(collection);
    }

    @Override
    public ListLinksResponse getAllLinks() {
        List<LinkResponse> collection = jooqLinkRepository.getAllLinks();
        return new ListLinksResponse(collection);
    }

    @Override
    public ListLinksResponse getAllUncheckedLinks() {
        List<LinkResponse> collection = jooqLinkRepository.getAllUncheckedLinks();
        return new ListLinksResponse(collection);
    }

}
