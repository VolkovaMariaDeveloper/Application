package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface IBot extends  UpdatesListener {
   // <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request);

    @Override
    int process(List<Update> updates);

    void start() throws TelegramApiException;

}
