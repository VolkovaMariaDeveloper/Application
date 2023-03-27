package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class TgChatApiController {

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdPost(@PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> tgChatIdDelete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

}
