package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UntrackCommand implements ICommand {
    @Override
    public String command() {
        return "/track";
    }
    public static final String ERROR_MESSAGE = "Не удалось удалить ссылку из списка отслеживаемых ссылок, возможно вы на нее не подписаны.";

    @Override
    public String description() {
        return "Вы успешно отписались от ссылки, теперь вы не будете получать уведомления об изменениях";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        return new SendMessage(chatId,description());
    }
}