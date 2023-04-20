package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    @Autowired
    private JdbcLinkRepository linkRepository;

    JdbcLinkService(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public JdbcLinkResponse add(long tgChatId, String url) {

        long id = linkRepository.add(tgChatId, url);
        return new JdbcLinkResponse(id, null, url, OffsetDateTime.now());
    }

    @Override
    public JdbcLinkResponse remove(long tgChatId, String url) {
        long id = linkRepository.remove(tgChatId, url);
        return new JdbcLinkResponse(id, null, url, OffsetDateTime.now());
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<JdbcLinkResponse> collection = linkRepository.findAll(tgChatId);
        return new ListLinksResponse(collection);
    }

    @Override
    public Collection<JdbcLinkResponse> getAllLinks() {
        List<JdbcLinkResponse> collection = linkRepository.getAllLinks();
        return collection;
    }

    @Override
    public Collection<JdbcLinkResponse> getAllUncheckedLinks() {
        List<JdbcLinkResponse> collection = linkRepository.getAllUncheckedLinks();
        return collection;
    }
}
