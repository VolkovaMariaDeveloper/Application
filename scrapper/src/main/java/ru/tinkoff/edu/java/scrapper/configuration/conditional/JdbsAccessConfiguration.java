package ru.tinkoff.edu.java.scrapper.configuration.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbsAccessConfiguration {
    @Bean("jdbsLinkService")
    public LinkService linkService(){
        return new JdbcLinkService();
    }

    @Bean("jdbsLinkUpdater")
    public LinkUpdater linkUpdater(){
        return new JdbcLinkUpdater();
    }

    @Bean("jdbsTgChatService")
    public TgChatService tgChatService(){
        return new JdbcTgChatService();
    }
}
