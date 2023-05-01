package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import java.util.List;

@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Override
    public void register(long tgChatId) {
        jdbcChatRepository.add(tgChatId);}

    @Override
    public void unregister(long tgChatId) {
        jdbcChatRepository.remove(tgChatId);}

    @Override
    public List<Long> getChatIdsForLink(String link) {
        return jdbcChatRepository.findAll(link);
    }


}
