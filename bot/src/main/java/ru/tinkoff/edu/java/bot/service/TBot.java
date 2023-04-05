package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

import java.util.List;

@Component
public class TBot implements IBot {
    private final TelegramBot bot = new TelegramBot(System.getenv("TOKEN"));
    private final CommandContainer commandContainer;

    @Autowired
    public TBot(ScrapperClient scrapperClient) {
        this.commandContainer = new CommandContainer(scrapperClient);
        //this.bot.execute(new SetMyCommands(commandContainer.commandsArray));
       // bot.execute(new SetMyCommands(this.commandContainer.commandMap,new BotCommandScopeDefault(),null));
    }


    @Override
    public int process(List<Update> updates) {
        return 0;
    }

    @Override
    public void start() {
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
            return new String[]{words[0]};
        }
    }
}
