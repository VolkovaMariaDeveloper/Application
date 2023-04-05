package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;

import java.util.List;

//@RequiredArgsConstructor
@Service
public class ListCommand implements ICommand {

    private final ScrapperClient scrapperClient;
    public static final String ERROR_MESSAGE = "Список отслеживаемых ссылок пуст, для получения списка доступных команд введите /help";

    @Autowired
    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

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
        if (trackedLinks.isEmpty()) {
            return new SendMessage(chatId, ERROR_MESSAGE);
        } else {
            StringBuilder linksList = new StringBuilder(description());
            for (LinkResponse link : trackedLinks) {
                linksList.append(link.url()).append("\n\n");
            }
            return new SendMessage(chatId, linksList.toString());
        }
    }
}
