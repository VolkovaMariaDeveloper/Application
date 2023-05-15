package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

//@RequiredArgsConstructor
@Component
public class ListCommand implements ICommand {

    private final ScrapperClient scrapperClient;
    public static final String ERROR_MESSAGE = "Список отслеживаемых ссылок пуст, для получения списка доступных команд введите /help";
    private final String SUCCESSFUL_MESSAGE = "Вот ссылки, на которые вы подписаны: \n";

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
        return "вывести список отследиваемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        ListLinkResponse trackedLinks = scrapperClient.getTrackedLinks(chatId);

        //TODO вернуть список строк
//        if (trackedLinks.links().isEmpty()) {
        if (trackedLinks==null) {
            return new SendMessage(chatId, ERROR_MESSAGE);
        } else {
            StringBuilder linksList = new StringBuilder(SUCCESSFUL_MESSAGE);
            for (LinkResponse link : trackedLinks.links()) {
                linksList.append(link.url()).append("\n");
            }
            return new SendMessage(chatId, linksList.toString());
        }
    }
}
