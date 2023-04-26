package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    @Autowired
    private JdbcLinkRepository linkRepository;

    JdbcLinkService(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public LinkResponse add(long tgChatId, String url) {
//        List<LinkResponse> listLinks = linkRepository.findAll(tgChatId);
//        if(listLinks.contains(url)) {
//        return null;
//        }
        long id = linkRepository.add(tgChatId, url);
        int count = linkRepository.fillCount(url);
        return new LinkResponse(id, null, url, OffsetDateTime.now(),count);
    }

    @Override
    public LinkResponse remove(long tgChatId, String url) {
        long id = linkRepository.remove(tgChatId, url);
        int count = linkRepository.fillCount(url);
        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<LinkResponse> collection = linkRepository.findAll(tgChatId);
        return new ListLinksResponse(collection);
    }

    @Override
    public Collection<LinkResponse> getAllLinks() {
        List<LinkResponse> collection = linkRepository.getAllLinks();
        return collection;
    }

    @Override
    public Collection<LinkResponse> getAllUncheckedLinks() {
        List<LinkResponse> collection = linkRepository.getAllUncheckedLinks();
        return collection;
    }
}
