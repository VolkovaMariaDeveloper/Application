package ru.tinkoff.edu.java.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.command.*;

import java.util.HashMap;

import static ru.tinkoff.edu.java.bot.enums.CommandName.*;
@Service
//@RequiredArgsConstructor
public class CommandContainer implements ICommandContainer {

    public  HashMap<String, ICommand> commandMap;
    private final ICommand unknownCommand;

    @Autowired
    public CommandContainer(ScrapperClient scrapperClient) {
        commandMap = new HashMap<>();
        commandMap.put(START.toString(), new StartCommand());
        commandMap.put(HELP.toString(), new HelpCommand());
        commandMap.put(TRACK.toString(), new TrackCommand(scrapperClient));
        commandMap.put(UNTRACK.toString(), new UntrackCommand());
        commandMap.put(LIST.toString(), new ListCommand(scrapperClient));
        unknownCommand = new UnknownCommand();
    }

    @Override
    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
