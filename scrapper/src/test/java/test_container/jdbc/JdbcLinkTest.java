package test_container.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;
import test_container.IntegrationEnvironment;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class,
        properties = {"app.database-access-type=jdbc"})

public class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    JdbcTgChatService jdbcTgChatService;

    @Autowired
    JdbcLinkService  jdbcLinkService;

    @Transactional
    @Rollback
    @Test
    void addTest() {
        long tgChatId = 1L;
        String link = "https://github.com/VolkovaMariaDeveloper/Application";
        jdbcTgChatService.register(tgChatId);
        LinkResponse response =  jdbcLinkService.add(tgChatId, link);
        assertAll("Should return adding url and link id",
                () -> assertThat(response.url()).isEqualTo(link),
                () -> assertThat(response.id()).isEqualTo(tgChatId));
    }


    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long firstTgChat_id = 1L;
        long secondTgChat_id = 2L;
        String firstLink = "https://github.com/VolkovaMariaDeveloper/Application";
        String secondLink = "https://stackoverflow.com/questions/52653836";
        jdbcTgChatService.register(firstTgChat_id);
        jdbcTgChatService.register(secondTgChat_id);
        jdbcLinkService.add(firstTgChat_id, firstLink);
        jdbcLinkService.add(secondTgChat_id, secondLink);
        jdbcLinkService.remove(firstTgChat_id, firstLink);
        ListLinksResponse links =  jdbcLinkService.getAllLinks();

        assertThat(links.links().get(0).url()).isEqualTo(secondLink);
            }




    //с транзакцией почему-то result.next() = false, поэтому проверяла по возвращаемому значению, а не по наличию в таблице.
    //без     @Transactional и  @Rollback закомменированный код тест успешно проходит

    @Test
    @Transactional
    @Rollback
    void findAllByChatIdTest() {
        long firstTgChat_id = 1L;
        String firstLink = "https://github.com/VolkovaMariaDeveloper/Application";
        String secondLink = "https://stackoverflow.com/questions/52653836";

        Set<String> actualLinks = new HashSet<>();
        Set<String> expectedLinks = Set.of(firstLink, secondLink);
        jdbcTgChatService.register(firstTgChat_id);
        jdbcLinkService.add(firstTgChat_id, firstLink);
        jdbcLinkService.add(firstTgChat_id, secondLink);
        ListLinksResponse list =  jdbcLinkService.findAllByChatId(firstTgChat_id);
        for (LinkResponse el : list.links()) {
            actualLinks.add(el.url());
        }
        assertThat(actualLinks).isEqualTo(expectedLinks);
    }


    @Test
    @Transactional
    @Rollback
    void getAllLinksTest() {
        long firstTgChat_id = 1L;
        String firstLink = "https://github.com/VolkovaMariaDeveloper/Application";
        String secondLink = "https://stackoverflow.com/questions/52653836";
        Set<String> actualLinks = new HashSet<>();
        Set<String> expectedLinks = Set.of(firstLink, secondLink);
        jdbcTgChatService.register(firstTgChat_id);
        jdbcLinkService.add(firstTgChat_id, firstLink);
        jdbcLinkService.add(firstTgChat_id, secondLink);
        ListLinksResponse list =  jdbcLinkService.getAllLinks();
        for (LinkResponse el : list.links()) {
            actualLinks.add(el.url());
        }
        assertThat(actualLinks).isEqualTo(expectedLinks);


    }

    //аналогично без аннотаций транзакций работает как надо, с ними result.next() = false

}
