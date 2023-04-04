package ru.tinkoff.edu.java.bot.command;

public enum CommandName {

    START("/start"),
    HELP( "/help"),
    TRACK("/track"),
    UNTRACK("/untrack"),
    LIST("/list");

    private final String cmd;

    CommandName(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return this.cmd;
    }
    public boolean equals(String cmd){
        return this.toString().equals(cmd);
    }
}
