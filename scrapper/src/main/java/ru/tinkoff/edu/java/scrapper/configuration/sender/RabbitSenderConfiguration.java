package ru.tinkoff.edu.java.scrapper.configuration.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.sender.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateMessageSender;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitSenderConfiguration {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    @Bean
    UpdateMessageSender updateMessageSender(){
        return new ScrapperQueueProducer(rabbitTemplate, queue);
    }
}
