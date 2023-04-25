package ru.tinkoff.edu.java.scrapper.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;


@RestController
@Log4j2

public class LinksApiController {
    @Autowired
    private JdbcLinkService jdbcLinkService;
    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> linksGetAll(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId) {
        ListLinksResponse linkResponse = jdbcLinkService.listAll(tgChatId);
        return ResponseEntity.ok(linkResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<JdbcLinkResponse> linksAdd(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        log.info("Adding link for chat: {}", tgChatId);
        log.info("Request: {}", addLinkRequest);
        JdbcLinkResponse linkResponse = jdbcLinkService.add(tgChatId, addLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping("/links")

    public ResponseEntity<JdbcLinkResponse> linksDelete(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        JdbcLinkResponse linkResponse = jdbcLinkService.remove(tgChatId, removeLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

}
