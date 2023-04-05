package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;
import ru.tinkoff.edu.java.bot.service.CommandContainer;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ListCommandTest {
    @Autowired
    public CommandContainer container;


    @Test
    void test_listCommands_is_empty() {
        long id = new Random().nextLong();

        //Mockito.when(scrapperClient.getTrackedLinks(id)).thenReturn(new ListLinkResponse(new ArrayList<>()));
        Chat chat = new Chat();
        ReflectionTestUtils.setField(chat, "id", id);
        Message message = new Message();
        ReflectionTestUtils.setField(message, "chat", chat);
        Update update = new Update();
        ReflectionTestUtils.setField(update, "message", message);

        ICommand result = container.retrieveCommand("/list");
        SendMessage messageFromBot = result.handle(update);
        String text = messageFromBot.getParameters().get("text").toString();
        //System.out.println(messageFromBot.getParameters().get("text").toString());
        assertThat(text).isEqualTo(ListCommand.ERROR_MESSAGE);

    }
    ListCommand listCommand;

    @Mock
    ScrapperClient scrapperClient;
    @BeforeEach
    void beforeStart(){
        listCommand = new ListCommand(scrapperClient);
    }
    @Test
    void test_listCommands(){
        long id = new Random().nextLong();
        Chat chat = new Chat();
        ReflectionTestUtils.setField(chat, "id", id);
        Message message = new Message();
        ReflectionTestUtils.setField(message, "chat", chat);
        Update update = new Update();
        ReflectionTestUtils.setField(update, "message", message);

        LinkResponse firstUrl = new LinkResponse(0, "https://github.com/VolkovaMariaDeveloper/Application/");
        ListLinkResponse listLinkResponse = new ListLinkResponse(List.of(firstUrl));

        when(scrapperClient.getTrackedLinks(id)).thenReturn(listLinkResponse);
        SendMessage request = listCommand.handle(update);

        assertThat(request.getParameters().get("text")).isEqualTo("""
                Вот ссылки, на которые вы подписаны:\s
                https://github.com/VolkovaMariaDeveloper/Application/
                """);
    }
}


