package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Links;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class JpaChatService implements TgChatService {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Override
    public void register(long tgChatId) {
        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setTrackedLinks(new HashSet<>());
        jpaChatRepository.saveAndFlush(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        Chat chat = jpaChatRepository.findById(tgChatId).get();
        jpaChatRepository.deleteById(tgChatId);
        jpaChatRepository.saveAndFlush(chat);
    }

    @Override
    public List<Long> getChatIdsForLink(String url) {
        Links link = jpaLinkRepository.findByUrl(url);
        Set<Chat> subscribers = link.getSubscribers();
        List<Long> ids = new ArrayList<>();
        for (Chat chat : subscribers) {
            ids.add(chat.getId());
        }
        return ids;
    }
}
