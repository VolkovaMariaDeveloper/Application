package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    @Autowired
    JooqLinkRepository jooqLinkRepository;
    @Override
    public LinkResponse add(long tgChatId, String url) {
        long id = jooqLinkRepository.add(tgChatId, url);
        int count = jooqLinkRepository.fillCount(url);
        return new LinkResponse(id, null, url, OffsetDateTime.now(),count);
    }

    @Override
    public LinkResponse remove(long tgChatId, String url) {
        long id = jooqLinkRepository.remove(tgChatId, url);
        int count = jooqLinkRepository.fillCount(url);
        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkResponse> collection = jooqLinkRepository.findAll(tgChatId);
        return new ListLinksResponse(collection);
    }

    @Override
    public Collection<LinkResponse> getAllLinks() {
        List<LinkResponse> collection = jooqLinkRepository.getAllLinks();
        return collection;
    }

    @Override
    public Collection<LinkResponse> getAllUncheckedLinks() {
        return null;
    }

}
