package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.command.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.tinkoff.edu.java.bot.command.CommandName.*;
@RequiredArgsConstructor

public class CommandContainer implements ICommandContainer {
    public  HashMap<String, ICommand> commandMap;
    private final ICommand unknownCommand;
    public List<BotCommand> commandsArray = new ArrayList<>();
    private ScrapperClient scrapperClient;



    public CommandContainer() {
        commandMap = new HashMap<>();
        ICommand command = new StartCommand();
        commandsArray.add(command.toApiCommand());
        commandMap.put(START.toString(), command);
        command = new HelpCommand();
        commandsArray.add(command.toApiCommand());
        commandMap.put(HELP.toString(), command);
        command = new TrackCommand();
        commandsArray.add(command.toApiCommand());
        commandMap.put(TRACK.toString(), command);
        command = new UntrackCommand();
        commandsArray.add(command.toApiCommand());
        commandMap.put(UNTRACK.toString(), command);
        command = new ListCommand(scrapperClient);
        commandsArray.add(command.toApiCommand());
        commandMap.put(LIST.toString(), command);


        unknownCommand = new UnknownCommand();

    }




    @Override
    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
