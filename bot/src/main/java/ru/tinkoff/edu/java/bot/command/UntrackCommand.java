package ru.tinkoff.edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinkResponse;

@Component
public class UntrackCommand implements ICommand {
    public static final String ERROR_MESSAGE = "Данная ссылка и так не отслеживается";
    public static final String ERROR_REQUEST_MESSAGE =
        "Ссылка не удалена из списка отслеживаемых ссылок, введите ее сразу после команды /untrack";
    @Autowired
    private final ScrapperClient scrapperClient;
    private final String SUCCESSFUL_MESSAGE =
        "Вы успешно отписались от ссылки, теперь вы не будете получать уведомления об изменениях";

    public UntrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();
        String[] words = splitMessageIntoWords(message);
        if (words[1] == null) {
            return new SendMessage(chatId, ERROR_REQUEST_MESSAGE);
        } else {
            ListLinkResponse listLinks = scrapperClient.getTrackedLinks(chatId);
            for (LinkResponse link : listLinks.links()) {
                if (link.url().equals(words[1])) {

                    scrapperClient.removeLinkFromTrack(String.valueOf(chatId), words[1]);
                    return new SendMessage(chatId, SUCCESSFUL_MESSAGE);
                }

            }
            return new SendMessage(chatId, ERROR_MESSAGE);
        }
    }

    private String[] splitMessageIntoWords(String message) {
        if (message == null) {
            return new String[0];
        }
        String[] words = message.split(" ", 2);
        if (words.length > 1) {
            return words;
        } else {
            return new String[] {words[0], null};
        }
    }
}
