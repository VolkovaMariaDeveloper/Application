package test_container.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class)
public class JpaChatTest extends IntegrationEnvironment {
    @Autowired
    JpaTgChatService jpaTgChatService;
    @Autowired
    JpaChatRepository jpaChatRepository;

    @Autowired
    JpaLinkService jpaLinkService;

    @Transactional
    @Rollback
    @Test
    void registerTest() {
        long tgChatId = 1;

        jpaTgChatService.register(tgChatId);
        List<Chat> response = jpaChatRepository.findAll();
        Chat chat = new Chat();
        chat.setId(tgChatId);
        List<Chat> expectedResponse = new ArrayList<>();
        expectedResponse.add(chat);
        assertThat(response.get(0).getId()).isEqualTo(expectedResponse.get(0).getId());
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
        List<Chat> response = jpaChatRepository.findAll();
        Chat chat1 = new Chat();
        chat1.setId(1L);
        Chat chat3 = new Chat();
        chat3.setId(3L);
        List<Chat> expectedResponse = new ArrayList<>();
        expectedResponse.add(chat1);
        expectedResponse.add(chat3);
        assertThat(response.get(0).getId()).isEqualTo(expectedResponse.get(0).getId());
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
        List<Long> ListIds = jpaTgChatService.getChatIdsForLink(url);
        Set<Long> response = new HashSet<>(ListIds);

        assertThat(response).isEqualTo(tgChatIds);
    }
}
