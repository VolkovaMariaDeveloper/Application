package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Override
    public int update(int count, String link) {
        linkRepository.updateLinks(count, link);

        return 0;
    }
}
