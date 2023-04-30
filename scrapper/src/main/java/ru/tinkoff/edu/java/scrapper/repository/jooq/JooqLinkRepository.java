package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINKS;
@Component
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext context;
    @Autowired
    // @Qualifier("GitHubService")
    private final GitHubClient gitHubClient;
    @Autowired
    //@Qualifier("StackOverflowService")
    private final StackOverflowClient stackOverflowClient;

    @Transactional
    public long add(long tgChatId, String link, int count) {
        LocalDateTime now = OffsetDateTime.now().toLocalDateTime();

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

//    public int fillCount(String url) {
//        ParserResult result = LinkParser.parseLink(url);
//        if (result instanceof GitHubParserResult) {
//            Pair<String, String> pair = ((GitHubParserResult) result).pairUserRepository;
//            String user = pair.getKey();
//            String repo = pair.getValue();
//            return gitHubClient.fetchRepository(user, repo).size();
//        } else if (result instanceof StackOverflowParserResult) {
//            String questionId = ((StackOverflowParserResult) result).idQuestion;
//            long id = Long.parseLong(questionId);
//            StackOverflowResponse.StackOverflowResponseItem[] list = stackOverflowClient.fetchQuestion(id).items();
//            return list[0].answer_count();
//        } else return -1;
//    }

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

//Проблема со сравнением дат

//    public List<LinkResponse> getAllUncheckedLinks() {
//        String linksSql = """
//                select id, url, last_check_time, count
//                from links
//                where (now()-last_check_time) > (50 * '1 sec'::interval)
//                """;
//        OffsetDateTime now = OffsetDateTime.now();
//
//        return context.select(LINKS.fields())
//                .from(LINKS)
//                .where((now.minus(LINKS.LAST_CHECK_TIME)).greaterThan(50 * '1 sec'::interval))           ////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                .fetchInto(LinkResponse.class);
//    }

    public void updateLinks(int count, String link){
        LocalDateTime now = OffsetDateTime.now().toLocalDateTime();
        context.update(LINKS)
                .set(LINKS.COUNT,count)
                .set(LINKS.LAST_CHECK_TIME,now)
                .where(LINKS.URL.eq(link));

    }
}
