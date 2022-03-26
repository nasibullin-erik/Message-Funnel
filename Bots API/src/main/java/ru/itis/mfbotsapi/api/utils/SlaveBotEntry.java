package ru.itis.mfbotsapi.api.utils;

import lombok.Builder;
import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
@Builder
public class SlaveBotEntry implements ServerEntry {

    protected String token;
    protected Messenger messenger;
    protected String botName;
    protected SocketChannel socketChannel;

    public enum Messenger{
        TELEGRAM
    }

}
