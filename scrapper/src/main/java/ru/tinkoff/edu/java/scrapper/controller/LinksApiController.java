package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.Valid;
import org.openapitools.api.LinksApi;
import org.openapitools.model.AddLinkRequest;
import org.openapitools.model.LinkResponse;
import org.openapitools.model.ListLinksResponse;
import org.openapitools.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.Optional;

@RestController


public class LinksApiController implements LinksApi {



    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        var response = new LinkResponse();
        response.setId(1L);
        response.setUrl(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        var response = new ListLinksResponse();
        response.setLinks(null);
        response.setSize(4);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/links/{tgChatId}")
    @Override

    public ResponseEntity<LinkResponse> linksPost(@PathVariable("tgChatId") Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        var response = new LinkResponse();
        response.setId(1L);
        response.setUrl(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
        return ResponseEntity.ok(response);
    }
}
