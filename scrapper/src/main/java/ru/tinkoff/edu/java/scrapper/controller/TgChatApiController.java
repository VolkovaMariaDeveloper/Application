package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RestController
public class TgChatApiController {
    @Autowired
    private TgChatService tgChatService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdAdd(@PathVariable Long id) {
        tgChatService.register(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        tgChatService.unregister(id);
        return ResponseEntity.ok().build();
    }

}
