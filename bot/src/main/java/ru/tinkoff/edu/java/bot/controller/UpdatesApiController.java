package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@RestController
public class UpdatesApiController {

    @PostMapping("/updates")
    public ResponseEntity<Void> updatesPost(@RequestBody LinkUpdateRequest linkUpdate) {
        //Not implemented yet

        return ResponseEntity.ok().build();
    }

}
