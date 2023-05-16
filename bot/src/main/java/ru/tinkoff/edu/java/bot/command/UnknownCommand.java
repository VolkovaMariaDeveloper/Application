package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UnknownCommand implements ICommand {

    @Override
    public String command() {
        return null;
    }

    @Override
    public String description() {
        return "Неизвестная команда, для получания списка всех доступных команд введите /help";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        return new SendMessage(chatId, description());
    }
}
