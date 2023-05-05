package ru.tinkoff.edu.java.bot.service.update;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TBot;

@RabbitListener(queues = "${app.queueName}")
public class ScrapperQueueListener extends SendUpdates{
    public ScrapperQueueListener(TBot bot) {
        super(bot);
    }

    @RabbitHandler
    public void receiver(LinkUpdateRequest update) {
        this.send(update);
    }
}
