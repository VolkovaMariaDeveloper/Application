package ru.tinkoff.edu.java.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.update.ReceiverUpdates;

@Log4j2
@RestController
@RequiredArgsConstructor
public class UpdatesApiController {
    @Autowired
    private final ReceiverUpdates updates;

    @PostMapping("/updates")
    public ResponseEntity<Void> updatesPost(@RequestBody LinkUpdateRequest linkUpdate) {
        updates.send(linkUpdate);
        return ResponseEntity.ok().build();
    }

}
