package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.dto.response.JooqLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.mappers.JooqMapper;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    @Autowired
    JooqLinkRepository jooqLinkRepository;
    @Autowired
    private CheckUpdater checkUpdater;

    @Override
    public LinkResponse add(long tgChatId, String url) {
        int count = checkUpdater.fillCount(url);
        long id = jooqLinkRepository.add(tgChatId, url, count);
        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public LinkResponse remove(long tgChatId, String url) {
        int count = checkUpdater.fillCount(url);
        long id = jooqLinkRepository.remove(tgChatId, url);

        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public ListLinksResponse findAllByChatId(long tgChatId) {
        List<JooqLinkResponse> collection = jooqLinkRepository.findAll(tgChatId);
        return JooqMapper.mapList(collection);
    }

    @Override
    public ListLinksResponse getAllLinks() {
        List<JooqLinkResponse> collection = jooqLinkRepository.getAllLinks();
        return JooqMapper.mapList(collection);
    }

    @Override
    public ListLinksResponse getAllUncheckedLinks() {
        List<JooqLinkResponse> collection = jooqLinkRepository.getAllUncheckedLinks();
        return JooqMapper.mapList(collection);
    }

    public void removeAll() {
        jooqLinkRepository.removeAllLinks();
        jooqLinkRepository.removeAllChatLink();
    }
}
