package ru.tinkoff.edu.java.bot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.command.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.tinkoff.edu.java.bot.enums.CommandName.*;
@Component
//@RequiredArgsConstructor
public class CommandContainer implements ICommandContainer {

    public  HashMap<String, ICommand> commandMap;
    private final ICommand unknownCommand;
    public  List<BotCommand> listCommand = new ArrayList<>();




    @Autowired
    public CommandContainer(ScrapperClient scrapperClient) {
        commandMap = new HashMap<>();
        ICommand command;
        commandMap.put(START.toString(), command = new StartCommand());
        listCommand.add(command.toApiCommand());
        commandMap.put(HELP.toString(), command = new HelpCommand());
        listCommand.add(command.toApiCommand());
        commandMap.put(TRACK.toString(), command = new TrackCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        commandMap.put(UNTRACK.toString(), command = new UntrackCommand());
        listCommand.add(command.toApiCommand());
        commandMap.put(LIST.toString(), command = new ListCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        unknownCommand =  new UnknownCommand();

        listCommand.add(command.toApiCommand());
    }

    @Override
    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
