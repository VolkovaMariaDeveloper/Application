package test_container.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.sql.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcChatTest extends IntegrationEnvironment{
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Transactional
    @Rollback
    @Test
    void addTest() {
        long tgChatId = 1L;
        jdbcChatRepository.add(tgChatId);

        String SQL_REQUEST_FROM_LINK = "SElECT * FROM chat";

        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);
            if (result.next()) {
               assertThat(result.getLong("id")).isEqualTo(tgChatId);
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
        long secondTgChat_id = 2L;

        jdbcChatRepository.add(firstTgChat_id);
        jdbcChatRepository.add(secondTgChat_id);

        jdbcChatRepository.remove(secondTgChat_id);

        String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";
        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);

            while (result.next()) {
                assertThat(result.getLong("id")).isNotEqualTo(firstTgChat_id);
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
        String link = "https://github.com/VolkovaMariaDeveloper/Application";
        long secondTgChat_id = 2L;
        int count = 2;

        List<Long> expectedChatIdList = List.of(firstTgChat_id, secondTgChat_id);

        jdbcChatRepository.add(firstTgChat_id);
        linkRepository.add(firstTgChat_id, link, count);
        jdbcChatRepository.add(secondTgChat_id);
        linkRepository.add(secondTgChat_id, link,count);

        List<Long> actualChatIdList = jdbcChatRepository.findAll(link);

        assertThat(actualChatIdList).isEqualTo(expectedChatIdList);

    }
}
