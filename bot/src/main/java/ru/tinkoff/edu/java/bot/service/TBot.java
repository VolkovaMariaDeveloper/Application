package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

import java.util.List;

@Component
public class TBot extends TelegramLongPollingBot implements IBot {
    private static final String TOKEN = "TOKEN";
    private final TelegramBot bot = new TelegramBot(System.getenv(TOKEN));
    @Autowired
    private final CommandContainer commandContainer;
    private static final String BOT_NAME = "NewsBot";

    public TBot(ScrapperClient scrapperClient) {
        super(System.getenv(TOKEN));
        this.commandContainer = new CommandContainer(scrapperClient);
    }

    @Override
    public int process(List<Update> updates) {
        return 0;
    }

    @PostConstruct
    @Override
    public void start() {
//        try {
//            this.execute(new SetMyCommands(commandContainer.listCommand, new BotCommandScopeDefault(), null));
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        if (update.message() != null) {
            String message = update.message().text();
            String[] words = splitMessageIntoWords(message);
            bot.execute(commandContainer.retrieveCommand(words[0]).handle(update));
        }
    }

    private String[] splitMessageIntoWords(String message) {
        String[] words = message.split(" ", 2);
        if (words.length > 1) {
            return words;
        } else {
            return new String[] {words[0]};
        }
    }

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public void sendMessage(SendMessage message) {
        bot.execute(message);
    }
}
