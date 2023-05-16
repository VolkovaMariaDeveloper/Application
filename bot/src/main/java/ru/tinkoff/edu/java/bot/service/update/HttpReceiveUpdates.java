package ru.tinkoff.edu.java.bot.service.update;

import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TBot;

public class HttpReceiveUpdates extends ReceiverUpdates {
    public HttpReceiveUpdates(TBot bot) {
        super(bot);
    }

    public void receiver(LinkUpdateRequest update) {
        this.send(update);
    }
}
