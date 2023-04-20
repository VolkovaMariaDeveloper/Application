package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
@Component
public class ClientConfiguration {

    @Bean("gitHubClient")
    public WebClient GitHubClient(@Value("${github.base-url}") String url) {
        return  WebClient.builder()
                .baseUrl(url)
                .build();
    }
    @Bean("stackOverflowClient")
    public WebClient stackOverflowClient(@Value("${stackoverflow.base-url}") String url) {
        return WebClient.builder()
                .baseUrl(url)
                .build();}
    @Bean("botClient")
    public WebClient botClient(@Value("${bot.base-url}") String url) {
        return WebClient.builder()
                .baseUrl(url)
                .build();}
    @Bean
    public long schedulerIntervalMs(ApplicationConfig config) {
        long toMillis = config.scheduler().interval().toMillis();
        return toMillis;
    }
}
