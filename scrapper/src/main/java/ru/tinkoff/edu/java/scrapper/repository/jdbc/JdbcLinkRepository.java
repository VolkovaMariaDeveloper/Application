package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@EnableTransactionManagement
//@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationConfig config;




    JdbcLinkRepository(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager, ApplicationConfig config) {
        this.config = config;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager, TransactionDefinition.withDefaults());
        this.transactionTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
    }


    public long add(long tgChatId, String link, int count) {

        Long insertId = transactionTemplate.execute(status -> {


            String linksSql = """
                    insert into links(url, last_check_time, count)
                    values(?, now(), ?)
                    on conflict (url) do nothing
                    returning id 
                    """;
            String chat_linkSql = """
                    insert into chat_link(chat_id, link_id)
                    values(?,?)
                    on conflict do nothing
                    """;
            String idSql = "select id from links where url=?";
            List<Long> tempId = jdbcTemplate.query(linksSql, (rs, rn) -> rs.getLong("id"), link, count);
            Long id;
            if (tempId.isEmpty()) {
                id = jdbcTemplate.queryForObject(idSql, Long.class, link);
            } else {
                id = tempId.get(0);
            }
            //обновление в связывающей таблице
            jdbcTemplate.update(chat_linkSql, tgChatId, id);
            return id;
        });
        return Objects.requireNonNull(insertId);
    }


    public long remove(long tgChatId, String link) {
        String chat_linkSql = """
                delete from chat_link using links
                where id = link_id and chat_id = ? and url = ?
                returning id
                """;
        //удаление неотслеживаемой ссылки из таблицы всех ссылок
        String linksSql = """
                delete from links
                where id = ?
                """;

        long id = jdbcTemplate.queryForObject(chat_linkSql, Long.class, tgChatId, link);
        if(getFollowers(id)==0){
            jdbcTemplate.update(linksSql, id);
        }
        return Objects.requireNonNull(id);


    }

    public long getFollowers(long linkId) {
        String chat_linkSql = """
                select count (*)
                from chat_link
                where link_id = ?
                """;
        long countFollowers = jdbcTemplate.queryForObject(chat_linkSql, Long.class, linkId);
        return Objects.requireNonNull(countFollowers);

    }

    //все отслеживаемые ссылки чата
    public List<LinkResponse> findAll(long tgChatId) {
        String linksSql = """
                select id, url, last_check_time, count 
                from links
                join chat_link on link_id = id
                where chat_id = ?
                """;
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    int count = rs.getInt("count");
                    return new LinkResponse(id, null, link, lastCheck_Time, count);

                },
                tgChatId
        );
    }

    //Все сохраненные ссылки
    public List<LinkResponse> getAllLinks() {
        String linksSql = """
                select id, url, last_check_time, count 
                from links
                """;
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    int count = rs.getInt("count");
                    return new LinkResponse(id, null, link, lastCheck_Time, count);

                }
        );
    }

    //Давно не проверяемые ссылки
    public List<LinkResponse> getAllUncheckedLinks() {
        String linksSql = """
                select id, url, last_check_time, count 
                from links
                where last_check_time < ?
                """;
        OffsetDateTime  checkPeriod = OffsetDateTime.now().minusHours(config.checkPeriodHours());
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    int count = rs.getInt("count");
                    return new LinkResponse(id, null, link, lastCheck_Time, count);

                },
                checkPeriod
        );
    }


    private OffsetDateTime getOffsetDateTime(ResultSet rs, String column) throws SQLException {
        Instant instantId = rs.getTimestamp(column).toInstant();
        return OffsetDateTime.ofInstant(instantId, ZoneOffset.UTC);
    }

    public void updateLinks(int count, String link) {
        String chat_linkSql = """
                update links set count = ?, last_check_time = now()
                where url = ?
                """;
        jdbcTemplate.update(chat_linkSql, count, link);

    }

}
