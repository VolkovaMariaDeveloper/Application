package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j2
//@Repository("chatRepository")
@Component
@Primary
public class JdbcChatRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long add(long tgChatId) { //Добавление нового пользователя

        String sql = "insert into chat(id) values(?) returning id";
        List<Long> tempId = jdbcTemplate.query(sql, (rs, rn) -> rs.getLong("id"), tgChatId);

        return tempId.get(0);
    }

    public void remove(long tgChatId) {
        String linksSql = """
            delete from chat using chat_link
            where id = ? and chat_id = id
            """;
        jdbcTemplate.update(linksSql, tgChatId);
    }

    public void removeAll() {
        String linksSql = """
            delete from chat
            """;
        jdbcTemplate.update(linksSql);
    }

    public List<Long> findAllByLink(String link) {
        String sql = """
            select chat_id from chat_link
            join links on id = link_id
            where url = ?
            """;
        return jdbcTemplate.query(
            sql,
            (rs, rn) -> rs.getLong("chat_id"),
            link
        );
    }

    public List<Long> getAllChats() {
        String sql = """
            select id from chat

            """;
        return jdbcTemplate.query(
            sql,
            (rs, rn) -> rs.getLong("id")
        );
    }

}
