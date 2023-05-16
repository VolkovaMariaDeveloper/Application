package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Component
@RequiredArgsConstructor
public class StartCommand implements ICommand {
    @Autowired
    private final ScrapperClient scrapperClient;
    private static final String SUCCESSFUL_MESSAGE =
        "Вы успешно зарегистрировались, для подробной информации введите команду /help";

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "регистрация пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        scrapperClient.registerChat(String.valueOf(chatId));
        return new SendMessage(chatId, SUCCESSFUL_MESSAGE);
    }
}

