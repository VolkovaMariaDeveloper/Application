package ru.tinkoff.edu.java.scrapper.configuration.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgChatService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean("jooqLinkService")
    public LinkService linkService(){
        return new JooqLinkService();
    }

    @Bean("jooqLinkUpdater")
    public LinkUpdater linkUpdater(){
        return new JooqLinkUpdater();
    }

    @Bean("jooqTgChatService")
    public TgChatService tgChatService(){
        return new JooqTgChatService();
    }
}
