package test_container.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import test_container.IntegrationEnvironment;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jpa"})
@RequiredArgsConstructor
public class JpaLinkTest extends IntegrationEnvironment {
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
    void addTest() {
        long tgChatId = 1;
        String url = "http://localhost";
        jpaTgChatService.register(tgChatId);
        LinkResponse response = jpaLinkService.add(tgChatId, url);
        assertThat(response.url()).isEqualTo(url);
    }

    @Transactional
    @Rollback
    @Test
    void removeTest() {
        long tgChatId = 1;
        String url1 = "http://localhost";
        String url2 = "http://localhost/new";

        jpaTgChatService.register(tgChatId);
        jpaLinkService.add(tgChatId, url1);
        jpaLinkService.add(tgChatId, url2);

        jpaLinkService.remove(tgChatId, url1);

        ListLinksResponse listResponse = jpaLinkService.findAllByChatId(tgChatId);
        LinkResponse response = listResponse.links().get(0);
        assertThat(response.url()).isEqualTo(url2);
    }


    @Transactional
    @Rollback
    @Test
    void findAllByChatIdTest() {
        long tgChatId = 1;
        String url = "http://localhost";

        jpaTgChatService.register(tgChatId);
        jpaLinkService.add(tgChatId, url + "1");
        jpaLinkService.add(tgChatId, url + "2");
        jpaLinkService.add(tgChatId, url + "3");

        ListLinksResponse response = jpaLinkService.findAllByChatId(tgChatId);
        Set<String> setResponse= new HashSet<>();
        for(LinkResponse link: response.links()){
            setResponse.add(link.url());
        }
        Set<String> expectedResponse = Set.of(url + "1",url + "2",url + "3");
        assertThat(setResponse).isEqualTo(expectedResponse);
    }

}
