package ru.tinkoff.edu.java.bot.configuration.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.service.TBot;
import ru.tinkoff.edu.java.bot.service.update.HttpSendUpdates;
import ru.tinkoff.edu.java.bot.service.update.SendUpdates;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpSenderConfiguration {
    @Autowired
    private final TBot bot;

    @Bean
    public SendUpdates sendUpdates(){
        return new HttpSendUpdates(bot);
    }
}
