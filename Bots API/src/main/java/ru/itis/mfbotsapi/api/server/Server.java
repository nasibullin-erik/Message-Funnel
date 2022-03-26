package ru.itis.mfbotsapi.api.server;

import ru.itis.mfbotsapi.api.exceptions.ServerException;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public interface Server {
    public void start(InetSocketAddress tcpAddress) throws ServerException;
    void sendTCPFrame(TCPFrame tcpFrame) throws ServerException;
    void stop();
}
