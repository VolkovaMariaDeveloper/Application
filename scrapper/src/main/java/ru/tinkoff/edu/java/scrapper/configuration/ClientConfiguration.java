package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
@Component
public class ClientConfiguration {

    @Bean("gitHubWebClient")
    public WebClient gitHubClient(@Value("${github.base-url}") String url) {
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return  WebClient.builder()
                .baseUrl(url)
                .build();
    }
    @Bean("stackOverflowWebClient")
    public WebClient stackOverflowClient(@Value("${stackoverflow.base-url}") String url) {
        return WebClient.builder()
                .baseUrl(url)
                .build();}
    @Primary
    @Bean("botWebClient")
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
