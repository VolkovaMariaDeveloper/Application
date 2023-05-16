package ru.tinkoff.edu.java.scrapper.repository.jooq;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINKS;

@Component
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
        context.deleteFrom(CHAT)//.using(CHAT_LINK)
            .where(CHAT.ID.eq(tgChatId))
            //.and(CHAT_LINK.CHAT_ID.eq(CHAT.ID))
            .execute();
    }

    public void removeAll() {
        context.deleteFrom(CHAT)
            .execute();
    }

    public List<Long> findAllByLink(String link) {
        return context.select(CHAT_LINK.CHAT_ID)
            .from(CHAT_LINK)
            .join(LINKS).on(LINKS.ID.eq(CHAT_LINK.LINK_ID))
            .where(LINKS.URL.eq(link))
            .fetchInto(Long.class);
    }

    public List<Long> getAllChats() {
        return context.select(CHAT.ID)
            .from(CHAT)
            .fetchInto(Long.class);
    }
}
