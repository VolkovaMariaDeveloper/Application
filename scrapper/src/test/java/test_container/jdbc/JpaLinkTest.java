package test_container.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.CheckUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ScrapperApplication.class)
public class JpaLinkTest extends IntegrationEnvironment {

    AutoCloseable openMock;
    @Mock
    CheckUpdater checkUpdater;
    @Autowired
    JpaChatService jpaChatService;
    //    @Mock
//    Chat chat;
//    @Mock
//    Links link;
    @InjectMocks
    @Autowired
    JpaLinkService jpaLinkService;

    @BeforeEach
    void setup() {
        openMock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    //Не мокается count , time  и остальное, что можно было бы закокать:(
    @Transactional
    @Rollback
    @Test
    void addTest() {
        long tgChatId = 1;
        String url = "http://localhost";
        int count = 2;
        jpaChatService.register(tgChatId);

        Mockito.when(checkUpdater.fillCount(any(String.class))).thenReturn(count);

//        Mockito.when(chat.getTrackedLinks()).thenReturn(links);
//        Mockito.when(jpaChatRepository.findById(tgChatId)).thenReturn(Optional.of(chat));
//       // Mockito.when(jpaChatRepository.findById(tgChatId).orElseThrow()).thenReturn(chat);
//        Mockito.when(link.getSubscribers()).thenReturn(new HashSet<>(2));

        LinkResponse response = jpaLinkService.add(tgChatId, url);
        LinkResponse expectedResponse = new LinkResponse(tgChatId, null, url, response.lastCheckTime(), count);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Transactional
    @Rollback
    @Test
    void removeTest() {
        long tgChatId = 1;
        String url = "http://localhost";
        int count = 2;
        jpaChatService.register(tgChatId);
        jpaLinkService.add(tgChatId, url);
        LinkResponse response = jpaLinkService.remove(tgChatId, url);
        LinkResponse expectedResponse = new LinkResponse(tgChatId, null, url, response.lastCheckTime(), count);
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Transactional
    @Rollback
    @Test
    void findAllByChatIdTest() {
        long tgChatId = 1;
        String url = "http://localhost";
        int count = 2;
        jpaChatService.register(tgChatId);
        jpaLinkService.add(tgChatId, url + "1");
        jpaLinkService.add(tgChatId, url + "2");
        jpaLinkService.add(tgChatId, url + "3");
        OffsetDateTime time = null;
        ListLinksResponse response = jpaLinkService.findAllByChatId(tgChatId);
        ListLinksResponse expectedResponse = new ListLinksResponse(new ArrayList<>());
        expectedResponse.links().add(new LinkResponse(tgChatId, null, url + "1", time, count));
        expectedResponse.links().add(new LinkResponse(tgChatId, null, url + "2", time, count));
        expectedResponse.links().add(new LinkResponse(tgChatId, null, url + "3", time, count));
        assertThat(response).isEqualTo(expectedResponse);
    }
}
