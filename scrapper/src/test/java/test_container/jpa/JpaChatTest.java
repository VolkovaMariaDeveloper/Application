package test_container.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import test_container.IntegrationEnvironment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jpa"})
public class JpaChatTest extends IntegrationEnvironment {
    @Autowired
    JpaTgChatService jpaTgChatService;

    @Autowired
    JpaLinkService jpaLinkService;

    @BeforeEach
    void cleanDB() {
        jpaTgChatService.removeAll();
        jpaLinkService.removeAll();
    }
    @Transactional
    @Rollback
    @Test
    void registerTest() {
        long firstTgChatId = 1;
        long secondTgChatId = 2;
        jpaTgChatService.register(firstTgChatId);
        jpaTgChatService.register(secondTgChatId);
        List<Long> response = jpaTgChatService.getAllChats();
        List<Long> expectedResponse = List.of(firstTgChatId, secondTgChatId);
        assertThat(response).isEqualTo(expectedResponse);
    }
    @Transactional
    @Rollback
    @Test
    void unregisterTest() {
        List<Long> tgChatIds = List.of(1L, 2L, 3L);
        for (Long id : tgChatIds) {
            jpaTgChatService.register(id);
        }
        jpaTgChatService.unregister(2L);

        List<Long> response = jpaTgChatService.getAllChats();
        assertThat(response).isEqualTo(List.of(1L, 3L));
    }
    @Transactional
    @Rollback
    @Test
    void getChatIdsForLinkTest() {
        Set<Long> tgChatIds = Set.of(1L, 2L, 3L);
        String url = "http://localhost";
        for (Long id : tgChatIds) {
            jpaTgChatService.register(id);
            jpaLinkService.add(id, url);
        }
        List<Long> ListIds = jpaTgChatService.getAllChatByLink(url);
        Set<Long> response = new HashSet<>(ListIds);

        assertThat(response).isEqualTo(tgChatIds);
    }
}
