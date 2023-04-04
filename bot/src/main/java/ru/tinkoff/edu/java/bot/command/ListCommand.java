package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;

import java.util.List;
@RequiredArgsConstructor
@Component
public class ListCommand implements ICommand {

    private final ScrapperClient  scrapperClient;
    public static final String ERROR_MESSAGE = "Список отслеживаемых ссылок пуст, для получения списка доступных команд введите /help";
    @Override
    public String command() {
        return "/list";
    }


    @Override
    public String description() {
        return "Вот ссылки, на которые вы подписаны: \n\n";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        List<LinkResponse> trackedLinks = scrapperClient.getTrackedLinks(chatId).links();

        //TODO вернуть список строк
        if (trackedLinks.isEmpty()){
        return new SendMessage(chatId,ERROR_MESSAGE);}
        else {
            String linksList = description();
            for (LinkResponse link : trackedLinks){
                linksList += link.url() + "\n\n";
            }
            return new SendMessage(chatId, linksList);
        }
    }
}
