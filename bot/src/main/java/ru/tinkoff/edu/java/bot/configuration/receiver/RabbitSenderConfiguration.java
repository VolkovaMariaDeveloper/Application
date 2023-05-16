package ru.tinkoff.edu.java.bot.configuration.receiver;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.service.TBot;
import ru.tinkoff.edu.java.bot.service.update.ScrapperQueueListener;
import ru.tinkoff.edu.java.bot.service.update.ReceiverUpdates;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitSenderConfiguration {

    @Bean
    public ReceiverUpdates receiveUpdates(TBot bot){
        return new ScrapperQueueListener(bot);
    }
}
