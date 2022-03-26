package ru.itis.mfbotsapi.api;

import ru.itis.mfbotsapi.api.exceptions.ServerException;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactoryImpl;
import ru.itis.mfbotsapi.api.server.ConnectionServer;
import ru.itis.mfbotsapi.api.server.Server;
import ru.itis.mfbotsapi.api.utils.ConnectionServerKeyManager;
import ru.itis.mfbotsapi.api.utils.SlaveBotEntry;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.UUID;

public class TestServer {
    public static void main(String[] args) throws Throwable {
        ConnectionServer server = ConnectionServer.init(new ConnectionServerKeyManager(),
                new TCPFrameFactoryImpl((byte) 0XCC, (byte) 0xDD, 2048, 64, 0),
                "key05467",
//                "key05468",
                SlaveBotEntry.Messenger.TELEGRAM,
                new SlaveBotImpl()
        );
        InetSocketAddress tcpAddress = new InetSocketAddress("localhost", 5467);
//        InetSocketAddress tcpAddress = new InetSocketAddress("localhost", 5468);
        Runnable runnable = () ->
        {
            try {
                server.start(tcpAddress);
            } catch (ServerException e) {
                System.out.println("Ошибка по причине ошибка");
            }
        };
        new Thread(runnable).start();
        Scanner scanner = new Scanner(System.in);
        while (true){
            String command = scanner.nextLine();
            switch (command){
                case "/message":
                    server.sendTCPFrame(server.getTcpFrameFactory().createTCPFrame(3,
                            UUID.randomUUID().toString(),
//                            "@User1",
//                            "Nickname1",
//                            "#Message1"
                            "@User2",
                            "Nickname2",
                            "#Message2"
                    ));
                    break;
                case "/exit":
                    server.stop();
            }

        }
    }
}
