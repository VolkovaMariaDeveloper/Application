package ru.tinkoff.edu.java.bot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.command.HelpCommand;
import ru.tinkoff.edu.java.bot.command.ICommand;
import ru.tinkoff.edu.java.bot.command.ListCommand;
import ru.tinkoff.edu.java.bot.command.StartCommand;
import ru.tinkoff.edu.java.bot.command.TrackCommand;
import ru.tinkoff.edu.java.bot.command.UnknownCommand;
import ru.tinkoff.edu.java.bot.command.UntrackCommand;
import static ru.tinkoff.edu.java.bot.enums.CommandName.HELP;
import static ru.tinkoff.edu.java.bot.enums.CommandName.LIST;
import static ru.tinkoff.edu.java.bot.enums.CommandName.START;
import static ru.tinkoff.edu.java.bot.enums.CommandName.TRACK;
import static ru.tinkoff.edu.java.bot.enums.CommandName.UNTRACK;

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
        commandMap.put(START.toString(), command = new StartCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        commandMap.put(HELP.toString(), command = new HelpCommand());
        listCommand.add(command.toApiCommand());
        commandMap.put(TRACK.toString(), command = new TrackCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        commandMap.put(UNTRACK.toString(), command = new UntrackCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        commandMap.put(LIST.toString(), command = new ListCommand(scrapperClient));
        listCommand.add(command.toApiCommand());
        unknownCommand = new UnknownCommand();

        listCommand.add(command.toApiCommand());
    }

    @Override
    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
