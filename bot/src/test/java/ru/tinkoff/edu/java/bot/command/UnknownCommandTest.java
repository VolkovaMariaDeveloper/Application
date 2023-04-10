package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.service.CommandContainer;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class UnknownCommandTest {

    AutoCloseable openMock;
    @Mock
    ScrapperClient scrapperClient;
    @InjectMocks
    CommandContainer container;
    @BeforeEach
    void setup() {
        openMock = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    @Test
    void test_unknown_command() {
        long id = new Random().nextLong();
        Chat chat = new Chat();
        ReflectionTestUtils.setField(chat, "id", id);
        Message message = new Message();
        ReflectionTestUtils.setField(message, "chat", chat);
        Update update = new Update();
        ReflectionTestUtils.setField(update, "message", message);
        ICommand result = container.retrieveCommand("unknown");
        SendMessage messageFromBot = result.handle(update);
        String text = messageFromBot.getParameters().get("text").toString();
       // System.out.println(messageFromBot.getParameters().get("text").toString());
        assertThat(text).isEqualTo("Неизвестная команда, для получания списка всех доступных команд введите /help");

    }
}

