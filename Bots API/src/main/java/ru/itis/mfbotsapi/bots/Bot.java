package ru.itis.mfbotsapi.bots;

import ru.itis.mfbotsapi.bots.exceptions.InitBotException;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfbotsapi.bots.exceptions.StopBotException;

public interface Bot {

    void init() throws InitBotException;

    void start() throws StartBotException;

    void stop() throws StopBotException;

}
