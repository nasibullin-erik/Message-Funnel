package ru.itis.mfbotsapi.api.server;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.exceptions.*;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactory;
import ru.itis.mfbotsapi.api.utils.ServerKeyManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@Slf4j
public abstract class AbstractServer implements Server {

    protected Selector selector;
    protected ServerSocketChannel serverTCPChannel;
    protected UUID serverUuid;
    protected ServerKeyManager keyManager;
    protected SocketChannel client;
    protected TCPFrameFactory tcpFrameFactory;
    protected boolean isWork;

    @Override
    public void start(InetSocketAddress tcpAddress) throws ServerException {
        try {
            isWork = true;
            selector = Selector.open();

            serverTCPChannel = ServerSocketChannel.open();
            serverTCPChannel.bind(tcpAddress);
            serverTCPChannel.configureBlocking(false);
            serverTCPChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException ex){
            throw new ServerException("Cannot create server: connection exception.", ex);
        }
        while (isWork){
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        keyManager.register(this);
                    }
                    if (key.isReadable()) {
                        keyManager.read(this, key);
                    }
                    if (key.isWritable()) {
                        keyManager.write(this);
                    }
                    iterator.remove();
                }
            } catch (IOException ex){
                log.warn("Ошибка при запуске сервера : невозможно запустить сервер");
                throw new ServerException("Cannot create server: connection exception.", ex);
            } catch (KeyManagerException ex){
                log.warn("Ошибка при работе сервера : ошибка при обработке пакетов или соединений");
                throw new ServerException(ex.getMessage(), ex);
            } catch (ClientDisconnectException ex){
                ex.getSelectionKey().cancel();
                if (client!=null){
                    client = null;
                }
                log.info("Client " + ex.getSelectionKey().channel() + " was disconnected");
            } catch (ClosedSelectorException ex){
                log.info("Сервер был выключен");
            }
        }
    }

    @Override
    public void sendTCPFrame(TCPFrame tcpFrame) throws ServerException {
        try {
            tcpFrameFactory.writeTCPFrame(client, tcpFrame);
        } catch (TCPFrameFactoryException ex) {
            throw new ServerException(ex.getMessage(), ex);
        }
    }

    @Override
    public void stop() {
        isWork = false;
        try{
            serverTCPChannel.close();
            client.close();
            selector.close();
        } catch (Exception ex){
            //ignore
        }

    }



}
