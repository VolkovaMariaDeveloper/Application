package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@Service
@RequiredArgsConstructor
public class JooqLinkUpdater implements LinkUpdater {
    @Autowired
    private JooqLinkRepository jooqRepository;
    @Override
    public int update(int count, String link) {
        jooqRepository.updateLinks(count, link);
        return 0;
    }
}
