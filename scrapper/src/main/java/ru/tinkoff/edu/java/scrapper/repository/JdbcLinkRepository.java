package ru.tinkoff.edu.java.scrapper.repository;

import javafx.util.Pair;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.JdbcLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
@EnableTransactionManagement
//@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;
    @Autowired
    // @Qualifier("GitHubService")
    private final GitHubClient gitHubClient;

    @Autowired
    //@Qualifier("StackOverflowService")
    private final StackOverflowClient stackOverflowClient;

    JdbcLinkRepository(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager, LinkService linkService, GitHubClient gitHubClient, StackOverflowClient stackOverflowClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager, TransactionDefinition.withDefaults());
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;

        this.transactionTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
    }


    public long add(long tgChatId, String link) {

        Long insertId = transactionTemplate.execute(status -> {


            String linksSql = """
                    insert into links(url, last_check_time, count)
                    values(?, now(), ?)
                    returning id 
                    """;
            String chat_linkSql = """
                    insert into chat_link(chat_id, link_id)
                    values(?,?)
                    on conflict do nothing
                    """;
            String idSql = "select id from link where url=?";
            int count = fillCount(link);
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

    public int fillCount(String url) {
        ParserResult result = LinkParser.parseLink(url);
        if (result instanceof GitHubParserResult) {
            Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
            String user = pair.getKey();
            String repo = pair.getValue();
            return gitHubClient.fetchRepository(user, repo).size();
        } else if (result instanceof StackOverflowParserResult) {
            String questionId = ((StackOverflowParserResult) result).idQuestion;
            long id = Long.parseLong(questionId);
            StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
            return list[0].answer_count();
        } else return -1;
    }

    public long remove(long tgChatId, String link) {
        String chat_linkSql = """
                delete from chat_link using links
                where id = link_id and chat_id = ? and url = ?
                returning id
                """;
        try {
            long id = jdbcTemplate.queryForObject(chat_linkSql, Long.class, tgChatId, link);
            return Objects.requireNonNull(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException(e);//какое исключение должно быть? создать новое?
        }
    }

    public List<JdbcLinkResponse> findAll(long tgChatId) {
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
                    return new JdbcLinkResponse(id, null, link, lastCheck_Time, count);

                },
                tgChatId
        );
    }

    public List<JdbcLinkResponse> getAllLinks() {
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
                    return new JdbcLinkResponse(id, null, link, lastCheck_Time, count);

                }
        );
    }

    public List<JdbcLinkResponse> getAllUncheckedLinks() {
        String linksSql = """
                select id, url, last_check_time, count 
                from links
                where (now()-last_check_time) > (50 * '1 sec'::interval)
                """;
        return jdbcTemplate.query(
                linksSql,
                (rs, rn) -> {
                    long id = rs.getLong("id");
                    String link = rs.getString("url");
                    OffsetDateTime lastCheck_Time = getOffsetDateTime(rs, "last_check_time");
                    int count = rs.getInt("count");
                    return new JdbcLinkResponse(id, null, link, lastCheck_Time, count);

                }
        );
    }


    private OffsetDateTime getOffsetDateTime(ResultSet rs, String column) throws SQLException {
        Instant instantId = rs.getTimestamp(column).toInstant();
        return OffsetDateTime.ofInstant(instantId, ZoneOffset.UTC);
    }
    public void updateLinks(int count, String link){
        String chat_linkSql = """
                    update links set count = ?, last_check_time = now()
                    where url = ?
                    """;
        jdbcTemplate.update(chat_linkSql, count, link);
        jdbcTemplate.update(chat_linkSql, count, link);

    }

}
