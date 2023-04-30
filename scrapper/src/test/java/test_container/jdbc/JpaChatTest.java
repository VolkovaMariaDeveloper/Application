package test_container.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class)
public class JpaChatTest extends IntegrationEnvironment {
    @Autowired
    JpaChatService jpaChatService;
    @Autowired
    JpaChatRepository jpaChatRepository;

    @Transactional
    @Rollback
    @Test
    void addTest() {
        long tgChatId = 1;

        jpaChatService.register(tgChatId);
        List<Chat> response = jpaChatRepository.findAll();
        Chat chat = new Chat();
        chat.setId(tgChatId);
        List<Chat>  expectedResponse = new ArrayList<>();
        expectedResponse.add(chat);
        assertThat(response).isEqualTo(expectedResponse);
    }
}
