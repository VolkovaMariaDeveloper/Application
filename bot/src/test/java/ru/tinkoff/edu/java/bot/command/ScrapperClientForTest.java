package ru.tinkoff.edu.java.bot.command;

import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

import java.util.*;
import java.util.stream.Collectors;

public class ScrapperClientForTest {
    private final Map<String, List<String>> chatLinks =
            new HashMap<>();

    public boolean registerChat(String chatId) {
        chatLinks.putIfAbsent(chatId, new ArrayList<>());
        return true;
    }

    public ListLinkResponse getTrackedLinks(String chatId) {
        List<String> links = chatLinks.getOrDefault(chatId, Collections.emptyList());

        List<LinkResponse> linkResponses = links.stream()
                .map(link -> new LinkResponse(links.indexOf(link), link))
                .collect(Collectors.toList());

        return new ListLinkResponse(linkResponses, links.size());
    }
}
