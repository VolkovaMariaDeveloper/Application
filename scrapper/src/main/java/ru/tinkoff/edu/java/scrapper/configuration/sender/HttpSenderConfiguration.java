package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.service.sender.HttpUpdateMessageSender;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateMessageSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpSenderConfiguration {
    @Autowired
    BotClient botClient;

    @Bean
    public UpdateMessageSender updateMessageSender() {
        return new HttpUpdateMessageSender(botClient);
    }
}
