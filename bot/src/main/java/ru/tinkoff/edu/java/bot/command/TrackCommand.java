package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

public class TrackCommand implements ICommand {
    @Autowired
    private final ScrapperClient scrapperClient;

    public TrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "/track";
    }
    public static final String ERROR_MESSAGE = "Ссылка не добавлена в список отслеживаемых ссылок, возможно вы ее уже отслеживаете.";

    @Override
    public String description() {
        return "Вы успешно подписались на ссылку %s, теперь вы будете получать уведомления об изменениях";
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();
        String[]words= splitMessageIntoWords(message);
        if(words[1]==null){
            return new SendMessage(chatId,ERROR_MESSAGE);
        }
        else{
            scrapperClient.addLinkToTrack(String.valueOf(chatId),words[1]);
        return new SendMessage(chatId,String.format(description(),words[1]));}
    }

    private String[] splitMessageIntoWords(String message) {
        if (message == null) {
            return new String[0];
        }
        String[] words = message.split(" ", 2);
        if (words.length > 1) {
            return words;
        } else {
            return new String[]{words[0],null};
        }
    }
    //добавить проверку на
    // -корректность ввода ссылки
    // -регитрацию ссылки
}