package ru.tinkoff.edu.java.scrapper.configuration.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean("jpaLinkService")
    public LinkService linkService() {
        return new JpaLinkService();
    }

    @Bean("jpaLinkUpdater")
    public LinkUpdater linkUpdater() {
        return new JpaLinkUpdater();
    }

    @Bean("jpaTgChatService")
    public TgChatService tgChatService() {
        return new JpaTgChatService();
    }
}
