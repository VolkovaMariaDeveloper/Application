package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private CheckUpdater checkUpdater;

  //  public JdbcLinkService(JdbcLinkRepository linkRepository) {
//        this.linkRepository = linkRepository;
//    }

    @Override
    public LinkResponse add(long tgChatId, String url) {
//        List<LinkResponse> listLinks = linkRepository.findAll(tgChatId);
//        if(listLinks.contains(url)) {
//        return null;
//        }
        int count = checkUpdater.fillCount(url);
        long id = linkRepository.add(tgChatId, url, count);

        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public LinkResponse remove(long tgChatId, String url) {
        long id = linkRepository.remove(tgChatId, url);
        int count = checkUpdater.fillCount(url);
        return new LinkResponse(id, null, url, OffsetDateTime.now(), count);
    }

    @Override
    public ListLinksResponse findAllByChatId(long tgChatId) {
        List<LinkResponse> collection = linkRepository.findAll(tgChatId);
        return new ListLinksResponse(collection);
    }

    @Override
    public ListLinksResponse getAllLinks() {
        List<LinkResponse> collection = linkRepository.getAllLinks();
        return new ListLinksResponse(collection);
    }

    @Override
    public ListLinksResponse getAllUncheckedLinks() {
        List<LinkResponse> collection = linkRepository.getAllUncheckedLinks();
        return new ListLinksResponse(collection);
    }
}
