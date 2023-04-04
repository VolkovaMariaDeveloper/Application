package ru.tinkoff.edu.java.scrapper.controller;

import org.openapitools.model.AddLinkRequest;
import org.openapitools.model.LinkResponse;
import org.openapitools.model.ListLinksResponse;
import org.openapitools.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;


@RestController


public class LinksApiController {


    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> linksGet(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId) {
        //Not implemented yet
        return ResponseEntity.ok().build();
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> linksPost(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        //Not implemented yet
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/links")

    public ResponseEntity<LinkResponse> linksDelete(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        //Not implemented yet
        return ResponseEntity.ok().build();
    }

}
