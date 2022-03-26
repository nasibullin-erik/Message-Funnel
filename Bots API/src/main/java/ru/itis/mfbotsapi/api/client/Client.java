package ru.itis.mfbotsapi.api.client;


import ru.itis.mfbotsapi.api.exceptions.ClientException;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;

import java.net.InetSocketAddress;

public interface Client {
    void connect(InetSocketAddress serverAddress, String connectionToken) throws ClientException;
    void sendTCPFrame (String token, TCPFrame tcpFrame) throws ClientException;
    void disconnect(String targetToken);
    void start();
    void stop();
}
