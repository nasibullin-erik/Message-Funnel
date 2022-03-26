package ru.itis.mfbotsapi.api.utils;

import ru.itis.mfbotsapi.api.client.AbstractClient;
import ru.itis.mfbotsapi.api.exceptions.ClientDisconnectException;
import ru.itis.mfbotsapi.api.exceptions.KeyManagerException;

import java.nio.channels.SelectionKey;

public interface ClientKeyManager {
    void read(AbstractClient client, SelectionKey key) throws KeyManagerException, ClientDisconnectException;
    void write(AbstractClient client) throws KeyManagerException;
}
