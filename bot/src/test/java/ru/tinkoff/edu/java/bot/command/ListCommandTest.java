package ru.tinkoff.edu.java.bot.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.tinkoff.edu.java.bot.service.CommandContainer;

import static junit.framework.Assert.assertEquals;
@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    private static final Long USER_ID = 12345670L;
    private Update update;
    @Mock
    private  ScrapperClientForTest scrapperClient = new ScrapperClientForTest();
private CommandContainer container = new CommandContainer();
    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(USER_ID);

        Chat chat = new Chat();
        chat.setId(USER_ID);

        Message message = new Message();
        message.setFrom(user);
        message.setChat(chat);

        update = new Update();
        update.setMessage(message);

        scrapperClient.registerChat(String.valueOf(USER_ID));
    }
    @Test
    void handle_EmptyLinkList() {
        String response = container.retrieveCommand("/list").description();
        String expected = ListCommand.ERROR_MESSAGE;

        assertEquals(expected, response);
    }
}
