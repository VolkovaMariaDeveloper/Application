package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

public class JdbcLinkUpdater implements LinkUpdater {
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Override
    public int update(int count, String link) {
        linkRepository.updateLinks(count, link);

        return 0;
    }
}
