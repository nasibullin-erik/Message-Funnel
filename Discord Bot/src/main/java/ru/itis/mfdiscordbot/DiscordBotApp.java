package ru.itis.mfdiscordbot;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.itis.mfbotsapi.api.client.ManagementClient;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactoryImpl;
import ru.itis.mfbotsapi.api.utils.*;
import ru.itis.mfbotsapi.bots.MasterBot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

@Slf4j
public class DiscordBotApp {

    protected static MasterBot discordBot;
    protected static ManagementClient managementClient;

    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = PropertiesLoader.getInstance();

        try {
            discordBot = new DiscordBot(propertiesLoader.getProperty("discord.bot.token"));
            managementClient = ManagementClient.init(new ManagementClientKeyManager(),
                    new TCPFrameFactoryImpl((byte) 0XCC, (byte) 0xDD, 2048, 64, 0),
                    discordBot
            );
            discordBot.init();
        } catch (StartBotException e) {
            log.error(e.getMessage());
        }
    }

    public static void replyOnMessage(String token, Reply reply){
        managementClient.sendTCPFrame(token, managementClient.getTcpFrameFactory()
                .createTCPFrame(4, UUID.randomUUID().toString(),
                        reply.getUserId(), reply.getMessage()));
    }

    public static void handleNewConfig(List<String> tokens){
        managementClient.stop();
        for (SlaveBotEntry entry : managementClient.getSlavesSet()){
            if (!tokens.contains(entry.getToken())){
                managementClient.disconnect(entry.getToken());
            }
        }
        List<String> currentTokens = managementClient.getSlavesSet().stream()
                .map(SlaveBotEntry::getToken)
                .collect(Collectors.toList());
        for (String token : tokens){
            if (!currentTokens.contains(token)){
                try{
                    managementClient.connect(getAddressByToken(token), token);
                    discordBot.sendMessage(NotificationMessage.builder()
                            .text("Соединение по токену " + token + " прошло успешно.")
                            .build());
                } catch (Exception ex){
                    log.warn("Cannot connect to bot with token " + token);
                    discordBot.sendMessage(ErrorMessage.builder()
                            .text("Не удалось подключиться по токену " + token + ".")
                            .build());
                }
            }
        }
        managementClient.start();
    }

    public static void establishNewConnection(String token){
        if (managementClient.getSlavesSet().stream()
                .anyMatch(slaveBotEntry -> slaveBotEntry.getToken().equals(token))){
            discordBot.sendMessage(WarningMessage.builder()
                    .text("Данное соединение уже установлено. Введите /connections, чтобы увидеть все соединения.")
                    .build());
        } else {
            try{
                managementClient.stop();
                managementClient.connect(getAddressByToken(token), token);
                managementClient.start();
                discordBot.sendMessage(NotificationMessage.builder()
                        .text("Соединение по токену " + token + " прошло успешно.")
                        .build());
            } catch (Exception ex){
                log.warn("Cannot connect to bot with token " + token);
                discordBot.sendMessage(ErrorMessage.builder()
                        .text("Не удалось подключиться по токену " + token + ".")
                        .build());
            }
        }
    }

    public static void obliterateConnection(String token){
        if (managementClient.getSlavesSet().stream()
                .anyMatch(slaveBotEntry -> slaveBotEntry.getToken().equals(token))){
            managementClient.disconnect(token);
            discordBot.sendMessage(NotificationMessage.builder()
                    .text("Соединение по токену " + token + " было разорвано.")
                    .build());
        } else {
            discordBot.sendMessage(WarningMessage.builder()
                    .text("Данного соединения не существует. Введите /connections, чтобы увидеть все соединения.")
                    .build());
        }
    }

    protected static InetSocketAddress getAddressByToken(String token){
        return new InetSocketAddress("localhost", Integer.parseInt(token.substring(token.length() - 5)));
    }

    public static List<String> getActiveTokens(){
        return managementClient.getSlavesSet().stream().map(SlaveBotEntry::getToken).collect(Collectors.toList());
    }
}