package ru.itis.mfbotsapi.api.server;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactory;
import ru.itis.mfbotsapi.api.utils.ServerKeyManager;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;
import ru.itis.mfbotsapi.bots.SlaveBot;

import java.util.UUID;

@Data
@SuperBuilder
public class ConnectionServer extends AbstractServer{

    protected String botToken;
    protected SlaveBotEntry.Messenger messenger;
    protected SlaveBot bot;

    public static ConnectionServer init(ServerKeyManager keyManager,
                                        TCPFrameFactory tcpFrameFactory,
                                        String botToken,
                                        SlaveBotEntry.Messenger messenger,
                                        SlaveBot bot){
        return ConnectionServer.builder()
                .keyManager(keyManager)
                .tcpFrameFactory(tcpFrameFactory)
                .serverUuid(UUID.randomUUID())
                .botToken(botToken)
                .messenger(messenger)
                .bot(bot)
                .build();
    }

}
