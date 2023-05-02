package ru.tinkoff.edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TBot;

import java.util.List;
@Log4j2
@RestController
@RequiredArgsConstructor
public class UpdatesApiController {
    @Autowired
    private final TBot bot;
    @PostMapping("/updates")
    public ResponseEntity<Void> updatesPost(@RequestBody LinkUpdateRequest linkUpdate) {
        List<Long> listChatIds = linkUpdate.tgChatId();
        for(Long chatIds:listChatIds) {
            bot.sendMessage(new SendMessage(chatIds, linkUpdate.description()));
            log.info("Update message sent for chat: {}", chatIds);
        }
        return ResponseEntity.ok().build();
    }

}
