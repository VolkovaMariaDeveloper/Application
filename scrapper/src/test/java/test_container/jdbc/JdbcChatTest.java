package test_container.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;
import test_container.IntegrationEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jdbc"})
public class JdbcChatTest extends IntegrationEnvironment {
    @Autowired
    JdbcTgChatService jdbcTgChatService;

    @Autowired
    JdbcLinkService jdbcLinkService;

    @BeforeEach
    void cleanDB() {
        jdbcTgChatService.removeAll();
        jdbcLinkService.removeAll();
    }

    @Transactional
    @Rollback
    @Test
    void addTest() {
        long firstTgChatId = 1L;
        long secondTgChatId = 2L;
        jdbcTgChatService.register(firstTgChatId);
        jdbcTgChatService.register(secondTgChatId);
        List<Long> expectedList = List.of(firstTgChatId, secondTgChatId);
        List<Long> result = jdbcTgChatService.getAllChats();

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long firstTgChatId = 1L;
        long secondTgChatId = 2L;

        jdbcTgChatService.register(firstTgChatId);
        jdbcTgChatService.register(secondTgChatId);
        jdbcTgChatService.unregister(secondTgChatId);
        List<Long> expectedList = List.of(firstTgChatId, secondTgChatId);
        List<Long> result = jdbcTgChatService.getAllChats();

        assertThat(result).isEqualTo(expectedList);

    }


    @Test
    @Transactional
    @Rollback
    void findAllByLinkTest() {
        long firstTgChatId = 1L;
        String link = "https://github.com/catsloveme/telegram-bot";
        long secondTgChatId = 2L;

        List<Long> expectedChatIdList = List.of(firstTgChatId, secondTgChatId);
        jdbcTgChatService.register(firstTgChatId);
        jdbcLinkService.add(firstTgChatId, link);
        jdbcTgChatService.register(secondTgChatId);
        jdbcLinkService.add(secondTgChatId, link);
        List<Long> actualChatIdList = jdbcTgChatService.getAllChatByLink(link);
        assertThat(actualChatIdList).isEqualTo(expectedChatIdList);

    }
}
