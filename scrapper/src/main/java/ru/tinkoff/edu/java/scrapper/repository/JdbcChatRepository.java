package ru.tinkoff.edu.java.scrapper.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository

public class JdbcChatRepository {
    private final JdbcTemplate jdbcTemplate;
    JdbcChatRepository(JdbcTemplate jdbcTemplate){
      this.jdbcTemplate =  jdbcTemplate;
    }

    public long add(long tgChatId){ //Добавление нового пользователя
        String sql = """
                insert into chat(id)
                values(?)
                returning id
                """;
        List<Long> tempId = jdbcTemplate.query(sql, (rs, rn) -> rs.getLong("id"), tgChatId);
       return tempId.get(0);
        }

    public long remove(long tgChatId){
        String linksSql = """
                delete from chat 
                where id = ?
                returning id
                """;
        try{
            long id = jdbcTemplate.queryForObject(linksSql, Long.class, tgChatId);
            return Objects.requireNonNull(id);
        }catch (EmptyResultDataAccessException e){
            throw new RuntimeException(e);//какое исключение должно быть? создать новое?
        }
    }

    public List<Long> findAll(String link){// найти все чаты, которые подписаны на ссылку? Или просто все чаты бота
        String sql = """
                select chat_id from chat_link
                join links on id = link_id 
                where url = ?
                
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rn) -> {
                    long id = rs.getLong("chat_id");
                    return id;
                },
                link
        );
    }


}
