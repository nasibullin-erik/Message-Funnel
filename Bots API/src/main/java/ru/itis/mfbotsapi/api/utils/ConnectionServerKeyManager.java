package ru.itis.mfbotsapi.api.utils;

import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.exceptions.*;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;
import ru.itis.mfbotsapi.api.server.AbstractServer;
import ru.itis.mfbotsapi.api.server.ConnectionServer;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

@Slf4j
public class ConnectionServerKeyManager implements ServerKeyManager {

    @Override
    public void register(AbstractServer server) throws KeyManagerException {

        SocketChannel client = null;
        try {
            client = server.getServerTCPChannel().accept();

            log.info("Было обнаружено новое соединение от клиента " + client.getRemoteAddress() + ".");

            TCPFrame tcpFrame = server.getTcpFrameFactory().readTCPFrame(client);

            if (tcpFrame!=null){
                if (tcpFrame.getType()==1){
                    Object[] userData = tcpFrame.getContent();

                    if (((ConnectionServer) server).getBotToken().equals(userData[1])){

                        UUID responseFrameId = UUID.randomUUID();
                        TCPFrame tcpFrameResponse = server.getTcpFrameFactory().createTCPFrame(2,
                                responseFrameId, ((ConnectionServer) server).getMessenger(),
                                ((ConnectionServer) server).getBot().getBotName());
                        server.getTcpFrameFactory().writeTCPFrame(client, tcpFrameResponse);

                        server.setClient(client);

                        client.configureBlocking(false);
                        client.register(server.getSelector(), SelectionKey.OP_READ);

                        log.info("Управляющий бот был успешно подключен к серверу.");
                    } else {
                        closeConnection(client);
                        log.info("Клиент" + client + "был отключен из-за неправильного токена.");
                    }
                } else {
                    closeConnection(client);
                    log.info("Клиент" + client + "был отключен из-за неправильного типа пакета.");
                }
            } else {
                closeConnection(client);
                log.info("Клиент" + client + "был отключен из-за неправильного формата пакета.");
            }
        } catch (IOException | TCPFrameFactoryException ex) {
            closeConnection(client);
            log.info("Клиент" + client + "был отключен из-за ошибки соединения");
        }  catch (IncorrectFCSException e) {
            //TODO reaction on incorrect frame
            closeConnection(client);
        }
    }

    private void closeConnection(SocketChannel connection){
        try{
            if (connection!=null){
                connection.close();
            }
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public void read(AbstractServer server, SelectionKey key) throws KeyManagerException, ClientDisconnectException {
            try{
                SocketChannel client = (SocketChannel) key.channel();
                TCPFrame tcpFrame = server.getTcpFrameFactory().readTCPFrame(client);
                UUID messageUuid = UUID.randomUUID();
                if (tcpFrame!=null){
                    Object[] messageContent = tcpFrame.getContent();
                    switch (tcpFrame.getType()){
                        case 4:
                            Reply reply = Reply.builder()
                                    .userId((String) messageContent[1])
                                    .message((String) messageContent[2])
                                    .build();
                            ((ConnectionServer) server).getBot().sendReply(reply);
                    }
                }
            } catch (TCPFrameFactoryException ex) {
                if (ex.getCause().getMessage().contains("принудительно разорвал существующее подключение")){
                    throw new ClientDisconnectException(key);
                } else {
                    throw new KeyManagerException(ex.getMessage(), ex);
                }
            } catch (IncorrectFCSException ex) {
                //TODO reaction on incorrect frame
            } catch (IllegalBlockingModeException | BufferUnderflowException ex){
                throw new ClientDisconnectException(key);
            }
    }

    @Override
    public void write(AbstractServer server) throws KeyManagerException {
        //**
    }
}
