package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TrackCommand implements ICommand {
    @Override
    public String command() {
        return "/track";
    }
    public static final String ERROR_MESSAGE = "Ссылка не добавлена в список отслеживаемых ссылок, возможно вы ее уже отслеживаете.";

    @Override
    public String description() {
        return "Вы успешно подписались на ссылку, теперь вы будете получать уведомления об изменениях";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        return new SendMessage(chatId,description());
    }
    //добавить проверку на
    // -корректность ввода ссылки
    // -регитрацию ссылки
}