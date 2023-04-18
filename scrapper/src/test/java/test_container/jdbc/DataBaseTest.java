package test_container.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
@SpringBootTest(classes= ScrapperApplication.class)
public class DataBaseTest extends IntegrationEnvironment {
    @Test
    void checkTables() {
        // Arrange
        Set<String> actualTables = new HashSet<>();
        Set<String> expectedTables = Set.of("chat", "links", "chat_link", "databasechangelog", "databasechangeloglock");

        // Act
        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {
            ResultSet result = connection.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
            while (result.next()) {
                actualTables.add(result.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Assert
        Assertions.assertTrue(actualTables.containsAll(expectedTables));
    }
    @Test
    void checkDatabase_is_running() {
        Assertions.assertTrue(DB_CONTAINER.isRunning());
    }
    @Test
    void checkMigrations_link_open(){
        String SQL_REQUEST_FROM_LINK = "SElECT * FROM links";

        try (Connection connection = DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword())) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_REQUEST_FROM_LINK);

            assertAll("Should return columns names",
                    () ->assertThat(result.getMetaData().getColumnName(1)).isEqualTo("url"),
                    () ->assertThat(result.getMetaData().getColumnName(2)).isEqualTo("id"));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

}
