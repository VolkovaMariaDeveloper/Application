package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINKS;
@Component
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext context;
    private ApplicationConfig config;

    @Transactional
    public long add(long tgChatId, String link, int count) {
        OffsetDateTime now = OffsetDateTime.now();

        Long id = context.insertInto(LINKS)
                .columns(LINKS.URL, LINKS.LAST_CHECK_TIME, LINKS.COUNT)
                .values(link, now, count)
                .onConflictDoNothing()
                .returningResult(LINKS.ID)
                .fetchOneInto(Long.class);
        if (id == null) {
            id = context.select(LINKS.ID)
                    .from(LINKS)
                    .where(LINKS.URL.eq(link))
                    .fetchOneInto(Long.class);
        }
        Objects.requireNonNull(id);

        context.insertInto(CHAT_LINK)
                .columns(CHAT_LINK.LINK_ID, CHAT_LINK.CHAT_ID)
                .values(id, tgChatId)
                .onConflictDoNothing()
                .execute();
        return id;
    }


    public long remove(long tgChatId, String link) {
        return context.deleteFrom(CHAT_LINK).using(LINKS)
                .where(LINKS.ID.eq(CHAT_LINK.LINK_ID))
                .and(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .and(LINKS.URL.eq(link))
                .returningResult(LINKS.ID)
                .fetchSingleInto(Long.class);
    }

    public List<LinkResponse> findAll(long tgChatId) {
        return context.select(LINKS.fields())
                .from(LINKS)
                .join(CHAT_LINK).on(CHAT_LINK.LINK_ID.eq(LINKS.ID))
                .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .fetchInto(LinkResponse.class);
    }

    //Все сохраненные ссылки
    public List<LinkResponse> getAllLinks() {
        return context.select(LINKS.fields())
                .select(LINKS)
                .fetchInto(LinkResponse.class);
    }
    public List<LinkResponse> getAllUncheckedLinks() {
        OffsetDateTime  checkPeriod = OffsetDateTime.now().minusHours(config.checkPeriodHours());
        return context.select(LINKS.fields())
                .from(LINKS)
                .where(LINKS.LAST_CHECK_TIME.greaterThan(checkPeriod))
                .fetchInto(LinkResponse.class);
    }

    public void updateLinks(int count, String link){
        OffsetDateTime now = OffsetDateTime.now();
        context.update(LINKS)
                .set(LINKS.COUNT,count)
                .set(LINKS.LAST_CHECK_TIME,now)
                .where(LINKS.URL.eq(link));

    }
}
