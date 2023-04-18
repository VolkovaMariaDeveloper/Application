package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component

public class StartCommand implements ICommand {
    private final String SUCCESSFUL_MESSAGE = "Вы успешно зарегистрировались, для подробной информации введите команду /help";
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
            return new SendMessage(chatId,SUCCESSFUL_MESSAGE);
        }
    }

