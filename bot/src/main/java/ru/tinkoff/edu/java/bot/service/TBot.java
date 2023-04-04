package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TBot implements IBot {
    private final TelegramBot bot = new TelegramBot(System.getenv("TOKEN"));
    private final CommandContainer commandContainer;

    public TBot() {
        this.commandContainer = new CommandContainer();
        //this.bot.execute(new SetMyCommands(commandContainer.commandsArray));

       // bot.execute(new SetMyCommands(this.commandContainer.commandMap,new BotCommandScopeDefault(),null));
    }

   // @Override
   // public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {



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

    @Override
    public void close() {

    }

    private void process(Update update) {
        if (update.message() != null) {
            String message = update.message().text();
            bot.execute(commandContainer.retrieveCommand(message).handle(update));
        }


    }
}
