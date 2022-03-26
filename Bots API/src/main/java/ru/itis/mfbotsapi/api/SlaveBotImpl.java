package ru.itis.mfbotsapi.api;

import ru.itis.mfbotsapi.api.utils.Reply;
import ru.itis.mfbotsapi.bots.SlaveBot;
import ru.itis.mfbotsapi.bots.exceptions.InitBotException;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfbotsapi.bots.exceptions.StopBotException;

public class SlaveBotImpl implements SlaveBot {

    @Override
    public void init() throws InitBotException {

    }

    @Override
    public void start() throws StartBotException {

    }

    @Override
    public void stop() throws StopBotException {

    }

    @Override
    public void sendReply(Reply reply) {
        System.out.println("От бота пришёл ответ: " + reply.getMessage());
    }

    @Override
    public String getBotName() {
        return "Лучший бот";
    }
}
