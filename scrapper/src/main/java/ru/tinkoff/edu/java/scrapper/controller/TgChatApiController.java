package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;


@RestController
public class TgChatApiController {
    JdbcTgChatService jdbcTgChatService = new JdbcTgChatService();
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdAdd(@PathVariable Long id) {
        jdbcTgChatService.register(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        jdbcTgChatService.unregister(id);
        return ResponseEntity.ok().build();
    }

}
