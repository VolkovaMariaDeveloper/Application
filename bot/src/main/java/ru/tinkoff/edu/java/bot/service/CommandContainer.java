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

    private final ICommand unknownCommand;
    public HashMap<String, ICommand> commandMap;
    public List<BotCommand> listCommand = new ArrayList<>();

    @Autowired
    public CommandContainer(ScrapperClient scrapperClient) {
        commandMap = new HashMap<>();
        ICommand command;
        command = new StartCommand(scrapperClient);
        commandMap.put(START.toString(), command);
        listCommand.add(command.toApiCommand());
        command = new HelpCommand();
        commandMap.put(HELP.toString(), command);
        listCommand.add(command.toApiCommand());
        command = new TrackCommand(scrapperClient);
        commandMap.put(TRACK.toString(), command);
        listCommand.add(command.toApiCommand());
        command = new UntrackCommand(scrapperClient);
        commandMap.put(UNTRACK.toString(), command);
        listCommand.add(command.toApiCommand());
        command = new ListCommand(scrapperClient);
        commandMap.put(LIST.toString(), command);
        listCommand.add(command.toApiCommand());
        unknownCommand = new UnknownCommand();

        listCommand.add(command.toApiCommand());
    }

    @Override
    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
