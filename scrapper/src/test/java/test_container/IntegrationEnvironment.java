package test_container;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
public abstract class IntegrationEnvironment {
    static final JdbcDatabaseContainer<?> DB_CONTAINER;
    private static final String IMAGE_NAME = "postgres:15";
    private static final String DB_NAME = "scrapper";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    private static final String CHANGELOG_DIR = "migrations";
    private static final String CHANGELOG_FILE = "master.yaml";


    static {
        DB_CONTAINER = new PostgreSQLContainer(IMAGE_NAME)
                .withDatabaseName(DB_NAME)
                .withUsername(USERNAME)
                .withPassword(PASSWORD);
        DB_CONTAINER.start();
        runMigration();
    }

    private static void runMigration() {
        var changelogPath = new File(".").toPath().toAbsolutePath().getParent().getParent()
                .resolve(CHANGELOG_DIR);
        try (var conn = DriverManager.getConnection(DB_CONTAINER.getJdbcUrl(), DB_CONTAINER.getUsername(), DB_CONTAINER.getPassword())) {
            var changelogDir = new DirectoryResourceAccessor(changelogPath);
            var db = new PostgresDatabase();
            db.setConnection(new JdbcConnection(conn));

            var liquibase = new Liquibase(CHANGELOG_FILE, changelogDir, db);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (FileNotFoundException | SQLException | LiquibaseException exception) {
            throw new RuntimeException(exception);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    }
}
