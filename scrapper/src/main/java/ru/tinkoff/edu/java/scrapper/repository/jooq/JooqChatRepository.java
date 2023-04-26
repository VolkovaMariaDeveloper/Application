package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.*;

@RequiredArgsConstructor
public class JooqChatRepository {
    private final DSLContext context;

    public long add(long tgChatId) {
        return context.insertInto(CHAT)
                .columns(CHAT.ID)
                .values(tgChatId)
                .returningResult(CHAT.ID)
                .fetchOneInto(Long.class);
    }

    public void remove(long tgChatId) {
        context.deleteFrom(CHAT).using(CHAT_LINK)
                .where(CHAT.ID.eq(tgChatId))
                .and(CHAT_LINK.CHAT_ID.eq(CHAT.ID))
                .returningResult(CHAT.ID)
                .fetchSingleInto(Long.class);
    }


    public List<Long> findAll(String link) {
        return context.select(CHAT_LINK.CHAT_ID)
                .from(CHAT_LINK)
                .join(LINKS).on(LINKS.ID.eq(CHAT_LINK.LINK_ID))
                .where(LINKS.URL.eq(link))
                .fetchInto(Long.class);
    }
}
