package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Links;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.mappers.JpaMapper;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private CheckUpdater checkUpdater;

    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private ApplicationConfig config;

    @Transactional
    @Override
    public LinkResponse add(long tgChatId, String url) {

        Links link;
        boolean exist = jpaLinkRepository.existsByUrl(url);

        if (!exist) {
            link = new Links();
            int count = checkUpdater.fillCount(url);

            link.setUrl(url);
            link.setCount(count);
            link.setSubscribers(new HashSet<>());
        } else {
            link = jpaLinkRepository.findByUrl(url);
        }
        Chat chat = jpaChatRepository.findById(tgChatId).orElseThrow();

        link.getSubscribers().add(chat);
        chat.getTrackedLinks().add(link);

        jpaChatRepository.flush();
        jpaLinkRepository.flush();
        return JpaMapper.map(link);
    }

    @Transactional
    @Override
    public LinkResponse remove(long tgChatId, String url) {
        Links link = jpaLinkRepository.findByUrl(url);
        Chat chat = jpaChatRepository.findById(tgChatId).orElse(null);
        assert chat != null;
        Set<Links> trackedLinks = chat.getTrackedLinks();
        trackedLinks.remove(link);
        link.getSubscribers().remove(chat);
        if (link.getSubscribers().isEmpty()) {
            jpaLinkRepository.removeByUrl(url);
        }
        jpaChatRepository.saveAndFlush(chat);
        jpaLinkRepository.saveAndFlush(link);
        return JpaMapper.map(link);
    }

    @Transactional(readOnly = true)
    @Override
    public ListLinksResponse findAllByChatId(long tgChatId) {
        Chat chat = jpaChatRepository.findById(tgChatId).get();
        Set<Links> trackedLinks = chat.getTrackedLinks();
        List<Links> links = new ArrayList<>(trackedLinks);
        return JpaMapper.mapList(links);
    }

    @Transactional(readOnly = true)
    @Override
    public ListLinksResponse getAllLinks() {
        List<Links> links = jpaLinkRepository.findAll();
        return JpaMapper.mapList(links);

    }

    @Transactional(readOnly = true)
    @Override
    public ListLinksResponse getAllUncheckedLinks() {
        OffsetDateTime checkPeriod = OffsetDateTime.now().minusHours(config.checkPeriodHours());
        List<Links> links = jpaLinkRepository.getAllUncheckedLinks(checkPeriod);
        return JpaMapper.mapList(links);
    }

    public void removeAll() {
        jpaLinkRepository.deleteAll();
    }
}
