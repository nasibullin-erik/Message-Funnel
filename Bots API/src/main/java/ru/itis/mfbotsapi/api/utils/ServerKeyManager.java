package ru.itis.mfbotsapi.api.utils;

import ru.itis.mfbotsapi.api.exceptions.ClientDisconnectException;
import ru.itis.mfbotsapi.api.exceptions.KeyManagerException;
import ru.itis.mfbotsapi.api.server.AbstractServer;

import java.nio.channels.SelectionKey;

public interface ServerKeyManager {
    void register(AbstractServer server) throws KeyManagerException;
    void read(AbstractServer server, SelectionKey key) throws KeyManagerException, ClientDisconnectException;
    void write(AbstractServer server) throws KeyManagerException;
}
