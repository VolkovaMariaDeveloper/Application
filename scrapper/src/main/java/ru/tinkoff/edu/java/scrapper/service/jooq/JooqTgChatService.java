package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Override
    public void register(long tgChatId) {
        jooqChatRepository.add(tgChatId);}

    @Override
    public void unregister(long tgChatId) {
        jooqChatRepository.remove(tgChatId);}

    @Override
    public List<Long> getChatIdsForLink(String link) {
        return jooqChatRepository.findAll(link);
    }

}
