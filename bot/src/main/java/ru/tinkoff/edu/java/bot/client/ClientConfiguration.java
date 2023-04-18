package ru.tinkoff.edu.java.bot.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean("scrapperWebClient")
    public WebClient scrapperWebClient(@Value("${scrapper.url}") String scrapperUrl) {
        return WebClient.builder()
                .baseUrl(scrapperUrl)
                .build();
    }
}
