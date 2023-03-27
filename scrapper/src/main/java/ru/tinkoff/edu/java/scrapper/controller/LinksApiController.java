package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<ListLinksResponse> linksGet(@NotNull Long tgChatId) {
        //Not implemented yet
        return ResponseEntity.ok().build();
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> linksPost(@NotNull Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        //Not implemented yet
        var response = new LinkResponse();
        response.setId(1L);
        response.setUrl(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/links")

    public ResponseEntity<LinkResponse> linksDelete(@NotNull Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        //Not implemented yet
        var response = new LinkResponse();
        response.setId(1L);
        response.setUrl(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
        return ResponseEntity.ok(response);
    }

}
