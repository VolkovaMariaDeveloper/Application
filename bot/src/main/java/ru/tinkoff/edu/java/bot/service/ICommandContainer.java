package ru.tinkoff.edu.java.bot.service;

import ru.tinkoff.edu.java.bot.command.ICommand;

public interface ICommandContainer {
    ICommand retrieveCommand(String commandIdentifier);

}
