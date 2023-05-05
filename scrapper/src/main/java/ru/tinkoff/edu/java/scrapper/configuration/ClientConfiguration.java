package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class ClientConfiguration {

    @Bean("gitHubWebClient")
    public WebClient gitHubClient(@Value("${github.base-url}") String url) {
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

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
                .withRenderSchema(false)
                .withRenderFormatted(true)
                .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

}
