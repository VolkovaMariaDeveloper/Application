package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.entity.Links;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Override
    public void update(int count, String url) {
        Links link = jpaLinkRepository.findByUrl(url);
        link.setCount(count);
    }
}
