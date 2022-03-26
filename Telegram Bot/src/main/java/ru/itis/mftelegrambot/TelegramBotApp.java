package ru.itis.mftelegrambot;

import java.net.InetSocketAddress;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.SlaveBotImpl;
import ru.itis.mfbotsapi.api.exceptions.ServerException;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactoryImpl;
import ru.itis.mfbotsapi.api.server.ConnectionServer;
import ru.itis.mfbotsapi.api.utils.ConnectionServerKeyManager;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;
import ru.itis.mfbotsapi.bots.Bot;
import ru.itis.mfbotsapi.bots.SlaveBot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mftelegrambot.utils.PropertiesConstants;
import ru.itis.mftelegrambot.utils.PropertiesLoader;
import ru.itis.mftelegrambot.bots.TelegramBot;

@Slf4j
public class TelegramBotApp {

    private static ConnectionServer server;

    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();

        try {
            SlaveBot telegramBot = new TelegramBot(propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_USERNAME),propertiesLoader.getProperty(PropertiesConstants.TELEGRAM_BOT_TOKEN));
            telegramBot.start();
            startServer(telegramBot);
        } catch (StartBotException e) {
            log.error(e.getMessage());
        }
    }



    private static void startServer(SlaveBot bot) {
        server = ConnectionServer.init(new ConnectionServerKeyManager(),
          new TCPFrameFactoryImpl((byte) 0XCC, (byte) 0xDD, 2048, 64, 0),
          PropertiesLoader.getInstance().getProperty(PropertiesConstants.TELEGRAM_BOT_LTOKEN),
          SlaveBotEntry.Messenger.TELEGRAM,
          bot
        );
        InetSocketAddress tcpAddress = new InetSocketAddress("localhost", Integer.parseInt(PropertiesLoader.getInstance().getProperty(PropertiesConstants.TELEGRAM_BOT_PORT)));
        Runnable runnable = () ->
        {
            try {
                server.start(tcpAddress);
            } catch (ServerException e) {
                e.printStackTrace();
                System.out.println("Ошибка по причине ошибка");
            }
        };
        new Thread(runnable).start();
    }

    public static void sendMessage(String userid, String userName, String text) {
        server.sendTCPFrame(server.getTcpFrameFactory().createTCPFrame(3,
          UUID.randomUUID().toString(),
          userid,
          userName,
          text
        ));
    }
}
