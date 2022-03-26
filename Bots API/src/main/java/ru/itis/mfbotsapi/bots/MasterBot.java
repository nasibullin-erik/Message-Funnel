package ru.itis.mfbotsapi.bots;

import ru.itis.mfbotsapi.api.utils.Message;

public interface MasterBot extends Bot {

    void sendMessage(Message message);

}
