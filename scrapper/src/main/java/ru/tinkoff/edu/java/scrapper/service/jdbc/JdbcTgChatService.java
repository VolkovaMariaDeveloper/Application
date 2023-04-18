package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
@Service
public class JdbcTgChatService implements TgChatService {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Override
    public void register(long tgChatId) {
        jdbcChatRepository.add(tgChatId);}

    @Override
    public void unregister(long tgChatId) {
        jdbcChatRepository.remove(tgChatId);}
}