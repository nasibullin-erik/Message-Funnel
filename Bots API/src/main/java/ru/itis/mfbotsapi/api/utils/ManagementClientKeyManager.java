package ru.itis.mfbotsapi.api.utils;

import ru.itis.mfbotsapi.api.client.AbstractClient;
import ru.itis.mfbotsapi.api.client.ManagementClient;
import ru.itis.mfbotsapi.api.exceptions.ClientDisconnectException;
import ru.itis.mfbotsapi.api.exceptions.IncorrectFCSException;
import ru.itis.mfbotsapi.api.exceptions.KeyManagerException;
import ru.itis.mfbotsapi.api.exceptions.TCPFrameFactoryException;
import ru.itis.mfbotsapi.api.protocol.TCPFrame;

import java.nio.BufferUnderflowException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ManagementClientKeyManager implements ClientKeyManager {

    @Override
    public void read(AbstractClient client, SelectionKey key) throws KeyManagerException, ClientDisconnectException {
        try{
            SocketChannel slave = (SocketChannel) key.channel();
            TCPFrame tcpFrame = client.getTcpFrameFactory().readTCPFrame(slave);
            Object[] messageContent = tcpFrame.getContent();
            SlaveBotEntry entry = null;
            for (SlaveBotEntry slaveBotEntry : ((ManagementClient) client).getSlavesSet()){
                if (slaveBotEntry.getSocketChannel().equals(slave)){
                    entry = slaveBotEntry;
                }
            }
            switch (tcpFrame.getType()){
                case 3:
                    BotMessage newBotMessage = BotMessage.builder()
                            .token(entry.getToken())
                            .messenger(entry.getMessenger())
                            .userId((String) messageContent[1])
                            .userNickname((String) messageContent[2])
                            .text((String) messageContent[3])
                            .botName(entry.getBotName())
                            .build();
                    ((ManagementClient) client).getBot().sendMessage(newBotMessage);
                    break;
            }
        } catch (TCPFrameFactoryException ex) {
            if (ex.getCause().getMessage().contains("принудительно разорвал существующее подключение")){
                throw new ClientDisconnectException(key);
            } else {
                throw new KeyManagerException(ex.getMessage(), ex);
            }
        } catch (IncorrectFCSException ex) {
            //TODO reaction on incorrect frame
        } catch (IllegalBlockingModeException | BufferUnderflowException | ClientDisconnectException ex){
            throw new ClientDisconnectException(key);
        }
    }

    @Override
    public void write(AbstractClient client) throws KeyManagerException {
        //**
    }
}
