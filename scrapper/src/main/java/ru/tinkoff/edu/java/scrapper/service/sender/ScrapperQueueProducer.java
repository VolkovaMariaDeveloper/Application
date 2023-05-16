package ru.tinkoff.edu.java.scrapper.service.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;


@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateMessageSender{
    private final RabbitTemplate template;
    private final Queue queue;
    public void send(LinkUpdateRequest update) {
        this.template.convertAndSend(queue.getName(), update);
    }
}