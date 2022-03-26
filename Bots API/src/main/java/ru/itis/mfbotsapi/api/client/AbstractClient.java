package ru.itis.mfbotsapi.api.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.itis.mfbotsapi.api.exceptions.*;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactory;
import ru.itis.mfbotsapi.api.utils.ClientKeyManager;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
public abstract class AbstractClient implements Client {

    protected Selector selector;
    protected ClientKeyManager keyManager;
    protected TCPFrameFactory tcpFrameFactory;
    protected boolean isWork;

}
