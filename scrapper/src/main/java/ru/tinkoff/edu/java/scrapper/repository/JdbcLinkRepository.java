package ru.tinkoff.edu.java.scrapper.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.tinkoff.edu.java.scrapper.dto.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Repository
@EnableTransactionManagement
//@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate ;
    JdbcLinkRepository(JdbcTemplate jdbcTemplate,PlatformTransactionManager transactionManager){
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager, TransactionDefinition.withDefaults());
        this.transactionTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
    }

    private final RowMapper<LinkResponse> rowMapperLinkResponse = new DataClassRowMapper<>(LinkResponse.class);
    private final RowMapper<ListLinksResponse> rowMapperListLinksResponse = new DataClassRowMapper<>(ListLinksResponse.class);
    @Transactional
    public long add(long tgChatId, String link) {
        Long insertId = transactionTemplate.execute(status -> {


            String linksSql = """
                    insert into links(url, last_check_time)
                    values(?, now())
                    on conflict do nothing
                    returning id 
                    """;
            String chat_linkSql = """
                    insert into chat_link(chat_id, link_id)
                    values(?,?)
                    on conflict do nothing
                    """;
            String idSql = "select id from link where url=?";

            List<Long> tempId = jdbcTemplate.query(linksSql, (rs, rn) -> rs.getLong("id"), link);
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
        String linksSql = """
                delete from chat_link using links
                where id = link_id and chat_id = ? and url = ?
                returning id
                """;
        try {
            long id = jdbcTemplate.queryForObject(linksSql, Long.class, tgChatId, link);
            return Objects.requireNonNull(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException(e);//какое исключение должно быть? создать новое?
        }
    }

    public List<JdbcLinkResponse> findAll(long tgChatId) {
        String linksSql = """
                select id, url, last_check_time 
                from links
                join chat_link on link_id = link_id
                where chat_id = ?
                """;
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    return new JdbcLinkResponse(id, link, lastCheck_Time);

                },
                tgChatId
        );
    }
    public List<JdbcLinkResponse> getAllLinks() {
        String linksSql = """
                select id, url, last_check_time 
                from links
                """;
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    return new JdbcLinkResponse(id, link, lastCheck_Time);

                }
        );
    }

    private OffsetDateTime getOffsetDateTime(ResultSet rs, String column) throws SQLException {
        Instant instantId = rs.getTimestamp(column).toInstant();
        return OffsetDateTime.ofInstant(instantId, ZoneOffset.UTC);
    }
}
