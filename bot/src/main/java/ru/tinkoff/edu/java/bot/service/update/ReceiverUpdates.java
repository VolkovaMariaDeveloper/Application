package ru.tinkoff.edu.java.bot.service.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public abstract class ReceiverUpdates {
    @Autowired
    private final TBot bot;
    public abstract void receiver(LinkUpdateRequest linkUpdate);
    public void send(LinkUpdateRequest linkUpdate){
        List<Long> listChatIds = linkUpdate.tgChatId();
        for(Long chatIds:listChatIds) {
            bot.sendMessage(new SendMessage(chatIds, linkUpdate.description()));
            log.info("Update message sent for chat: {}", chatIds);
        }
}}
