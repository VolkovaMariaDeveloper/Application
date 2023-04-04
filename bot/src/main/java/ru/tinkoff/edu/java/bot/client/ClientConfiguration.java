package ru.tinkoff.edu.java.bot.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public ScrapperClient scrapperClient(@Value("${app.scrapper_url:http://localhost:8081}") String scrapperUrl) {
        return new ScrapperClient(WebClient.builder()
                .baseUrl(scrapperUrl)
                .build());
    }
}
