package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public interface IBot extends AutoCloseable, UpdatesListener {
   // <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request);

    @Override
    int process(List<Update> updates);

    void start();

    @Override
    void close();
}
