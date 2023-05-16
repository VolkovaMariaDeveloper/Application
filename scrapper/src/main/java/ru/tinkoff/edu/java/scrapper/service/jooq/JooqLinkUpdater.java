package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@RequiredArgsConstructor
public class JooqLinkUpdater implements LinkUpdater {
    @Autowired
    private JooqLinkRepository jooqRepository;

    @Override
    public void update(int count, String link) {
        jooqRepository.updateLinks(count, link);
    }
}
