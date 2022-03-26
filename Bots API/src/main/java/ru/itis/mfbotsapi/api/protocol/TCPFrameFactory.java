package ru.itis.mfbotsapi.api.protocol;

import ru.itis.mfbotsapi.api.exceptions.IncorrectFCSException;
import ru.itis.mfbotsapi.api.exceptions.TCPFrameFactoryException;

import java.nio.channels.SocketChannel;

public interface TCPFrameFactory {
    TCPFrame createTCPFrame(int messageType, Object ... messageContent);
    TCPFrame readTCPFrame (SocketChannel serverTCPSocket) throws TCPFrameFactoryException, IncorrectFCSException;
    void writeTCPFrame (SocketChannel serverTCPSocket, TCPFrame tcpFrame)throws TCPFrameFactoryException;
}
