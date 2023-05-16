package ru.tinkoff.edu.java.scrapper.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@RestController
@Log4j2

public class LinksApiController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> linksGetAll(
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        ListLinksResponse linkResponse = linkService.findAllByChatId(tgChatId);
        return ResponseEntity.ok(linkResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> linksAdd(
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("Adding link for chat: {}", tgChatId);
        log.info("Request: {}", addLinkRequest);
        LinkResponse linkResponse = linkService.add(tgChatId, addLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping("/links")

    public ResponseEntity<LinkResponse> linksDelete(
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse linkResponse = linkService.remove(tgChatId, removeLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

}
