package test_container.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
//@ContextConfiguration(classes = ApplicationConfig.class)
public class JdbcLinkTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Rollback
    @Test
    void addTest() {
        long tgChatId = 1L;
        String link = "https://github.com/VolkovaMariaDeveloper/Application";
        jdbcChatRepository.add(tgChatId);
        long expectedId = linkRepository.add(tgChatId, link);

        String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";

        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);
            if (result.next()) {
                assertAll("Should return adding url and link id",
                        () -> assertThat(result.getString("url")).isEqualTo(link),
                        () -> assertThat(result.getLong("id")).isEqualTo(expectedId));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long firstTgChat_id = 1L;
        String firstLink = "https://github.com/VolkovaMariaDeveloper/Application";
        long secondTgChat_id = 2L;
        String secondLink = "https://stackoverflow.com/questions/52653836";

        jdbcChatRepository.add(firstTgChat_id);
        linkRepository.add(firstTgChat_id, firstLink);
        jdbcChatRepository.add(secondTgChat_id);
        linkRepository.add(secondTgChat_id, secondLink);

        linkRepository.remove(secondTgChat_id, secondLink);

        String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";
        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);

            while (result.next()) {
                assertThat(result.getString("url")).isNotEqualTo(secondLink);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


    //с транзакцией почему-то result.next() = false, поэтому проверяла по возвращаемому значению, а не по наличию в таблице.
    //без     @Transactional и  @Rollback закомменированный код тест успешно проходит

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        long firstTgChat_id = 1L;
        String firstLink = "https://github.com/VolkovaMariaDeveloper/Application";
        String secondLink = "https://stackoverflow.com/questions/52653836";
        Set<String> actualLinks = new HashSet<>();
        Set<String> expectedLinks = Set.of(firstLink, secondLink);
        jdbcChatRepository.add(firstTgChat_id);
        linkRepository.add(firstTgChat_id, firstLink);
        linkRepository.add(firstTgChat_id, secondLink);

        List<JdbcLinkResponse> list = linkRepository.findAll(firstTgChat_id);
        //       String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";
//        try (Connection connection = DriverManager.getConnection(
//                DB_CONTAINER.getJdbcUrl(),
//                DB_CONTAINER.getUsername(),
//                DB_CONTAINER.getPassword())) {
//            Statement statement = connection.createStatement();
//            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);
        for (JdbcLinkResponse el : list) {
            actualLinks.add(el.link());
        }

//            while(result.next()) {
//                actualLinks.add(result.getString("url"));
//            }
        assertThat(actualLinks).isEqualTo(expectedLinks);

//        } catch (SQLException exception) {
//            throw new RuntimeException(exception);
//        }
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
        jdbcChatRepository.add(firstTgChat_id);
        linkRepository.add(firstTgChat_id, firstLink);
        linkRepository.add(firstTgChat_id, secondLink);

        List<JdbcLinkResponse> list = linkRepository.getAllLinks();
               String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";
        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);
//        for (JdbcLinkResponse el : list) {
//            actualLinks.add(el.link());


            while(result.next()) {
                actualLinks.add(result.getString("url"));
            }
        assertThat(actualLinks).isEqualTo(expectedLinks);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
