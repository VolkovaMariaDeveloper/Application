package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements ICommand {

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return """
    NewsBot поможет вам отслеживать изменения в репозиториях GitHub и вопросах на StackOverflow.
    
    Для этого необходимо ввести команду /track и ссылку, на обновления которой вы хотите подписаться.
    Например:
    /track https://stackoverflow.com/questions/75927558
    
    Чтобы отписаться от рассылки введите команду /untrack и ссылку на вопрос или репозиторий.
    Например:
    /untrack https://stackoverflow.com/questions/75927558
    
    Для получения списка всех отслеживаемых ссылок введите команду /list.
    """;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        //TODO окно с доступными командами
        return new SendMessage(chatId,description());
    }
}
