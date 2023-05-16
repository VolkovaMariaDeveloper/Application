package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service("GitHubService")
//@RequiredArgsConstructor
public class GitHubClient {
    @Autowired
    @Qualifier("gitHubWebClient")
    private final WebClient gitHubClient;

    public GitHubClient(WebClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<String> fetchRepository(String user, String repository) {
        Mono<List<GitHubResponse>> response = gitHubClient.get()
            .uri("/repos/{user}/{repository}/branches", user, repository)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<GitHubResponse>>() {
            });
        List<GitHubResponse> branches = response.block();
        return branches.stream()
            .map(GitHubResponse::name)
            .collect(Collectors.toList());
    }
}
