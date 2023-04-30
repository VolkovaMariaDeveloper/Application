package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Links;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.mapper.FromLinksToLinkResponse;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private CheckUpdater checkUpdater;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Transactional
    @Override
    public LinkResponse add(long tgChatId, String url) {
        Links link = new Links();
        int count = checkUpdater.fillCount(url);

        link.setUrl(url);
        link.setCount(count);

        Optional<Chat> chatOp;
        chatOp = jpaChatRepository.findById(tgChatId);
        Chat chat = chatOp.orElseThrow();
        link.setSubscribers(new HashSet<>());
        link.getSubscribers().add(chat);
        chat.getTrackedLinks().add(link);

        jpaChatRepository.flush();
        jpaLinkRepository.flush();
        return FromLinksToLinkResponse.map(link);
    }


    @Override
    public LinkResponse remove(long tgChatId, String url) {
        Links link = jpaLinkRepository.findByUrl(url);
        Chat chat = jpaChatRepository.findById(tgChatId).orElse(null);
        Set<Links> trackedLinks = chat.getTrackedLinks();

        trackedLinks.remove(link);
        jpaChatRepository.saveAndFlush(chat);
        return FromLinksToLinkResponse.map(link);
    }

    @Override
    public ListLinksResponse findAllByChatId(long tgChatId) {
        Chat chat = jpaChatRepository.findById(tgChatId).get();
        Set<Links> trackedLinks = chat.getTrackedLinks();
        List<Links> links = new ArrayList<>(trackedLinks);
        return FromLinksToLinkResponse.mapList(links);
    }

    @Override
    public ListLinksResponse getAllLinks() {
        List<Links> links = jpaLinkRepository.findAll();
        return FromLinksToLinkResponse.mapList(links);

    }

    @Override
    public ListLinksResponse getAllUncheckedLinks() {
        return null;
    }
}
