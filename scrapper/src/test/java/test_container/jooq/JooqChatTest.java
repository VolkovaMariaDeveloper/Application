package test_container.jooq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgChatService;
import test_container.IntegrationEnvironment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jooq"})
public class JooqChatTest extends IntegrationEnvironment {
    @Autowired
    JooqTgChatService jooqTgChatService;
    @Autowired
    JooqLinkService jooqLinkService;

    @Transactional
    @Rollback
    @Test
    void registerTest() {
        long firstTgChatId = 1;
        long secondTgChatId = 2;
        jooqTgChatService.register(firstTgChatId);
        jooqTgChatService.register(secondTgChatId);
        List<Long> response = jooqTgChatService.getAllChats();
        List<Long> expectedResponse = List.of(firstTgChatId, secondTgChatId);
        assertThat(response).isEqualTo(expectedResponse);

    }

    @Transactional
    @Rollback
    @Test
    void unregisterTest() {
        List<Long> tgChatIds = List.of(1L, 2L, 3L);
        for (Long id : tgChatIds) {
            jooqTgChatService.register(id);
        }
        jooqTgChatService.unregister(2L);
        List<Long> response = jooqTgChatService.getAllChats();
        assertThat(response).isEqualTo(List.of(1L, 3L));
    }

    @Transactional
    @Rollback
    @Test
    void getChatIdsForLinkTest() {
        Set<Long> tgChatIds = Set.of(1L, 2L, 3L);
        String url = "http://localhost";
        for (Long id : tgChatIds) {
            jooqTgChatService.register(id);
            jooqLinkService.add(id, url);
        }
        List<Long> ListIds = jooqTgChatService.getAllChatByLink(url);
        Set<Long> response = new HashSet<>(ListIds);

        assertThat(response).isEqualTo(tgChatIds);
    }
}
