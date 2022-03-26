package ru.itis.mfbotsapi.bots;

import ru.itis.mfbotsapi.api.utils.Reply;

public interface SlaveBot extends Bot {

    void sendReply(Reply reply);
    String getBotName();

}
