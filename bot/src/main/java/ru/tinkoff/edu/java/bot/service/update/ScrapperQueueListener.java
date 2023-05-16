package ru.tinkoff.edu.java.bot.service.update;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TBot;

@Log4j2
@RabbitListener(queues = "${app.queueName}")
public class ScrapperQueueListener extends ReceiverUpdates {
    public ScrapperQueueListener(TBot bot) {
        super(bot);
    }

    @RabbitHandler
    public void receiver(LinkUpdateRequest update) {
        log.info("Got request: {}", update);
        this.send(update);
    }
}
