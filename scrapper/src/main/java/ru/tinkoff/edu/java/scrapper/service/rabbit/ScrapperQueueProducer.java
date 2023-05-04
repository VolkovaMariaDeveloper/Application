package ru.tinkoff.edu.java.scrapper.service.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    @Autowired
    private final RabbitTemplate template;
    private final Queue queue;
    public void send(LinkUpdateRequest update) {
        this.template.convertAndSend(queue.getName(), update);
    }
}