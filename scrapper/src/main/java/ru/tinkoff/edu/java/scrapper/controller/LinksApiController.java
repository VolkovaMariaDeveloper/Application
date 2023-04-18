package ru.tinkoff.edu.java.scrapper.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;


@RestController


public class LinksApiController {
    JdbcLinkService jdbcLinkService = new JdbcLinkService();

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> linksGetAll(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId) {
        ListLinksResponse linkResponse = jdbcLinkService.listAll(tgChatId);
        return ResponseEntity.ok(linkResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<JdbcLinkResponse> linksAdd(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        JdbcLinkResponse linkResponse = jdbcLinkService.add(tgChatId, addLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping("/links")

    public ResponseEntity<JdbcLinkResponse> linksDelete(@RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        JdbcLinkResponse linkResponse = jdbcLinkService.remove(tgChatId, removeLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

}
