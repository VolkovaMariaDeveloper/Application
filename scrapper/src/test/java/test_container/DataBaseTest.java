package test_container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseTest extends IntegrationEnvironment{
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
       // Assertions.assertTrue(actualTables.containsAll(expectedTables));
        assertThat(actualTables.toString()).isEqualTo(expectedTables.toString());
    }
    @Test
    void checkDatabase_is_running() {
        Assertions.assertTrue(DB_CONTAINER.isRunning());
    }


}
