package ru.itis.mfdiscordbot.bots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.security.auth.login.LoginException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import ru.itis.mfbotsapi.api.utils.*;
import ru.itis.mfbotsapi.bots.MasterBot;
import ru.itis.mfbotsapi.bots.exceptions.InitBotException;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mfbotsapi.bots.exceptions.StopBotException;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.bots.events.*;
import ru.itis.mfdiscordbot.config.BotConfig;
import ru.itis.mfdiscordbot.config.IdentityConfig;
import ru.itis.mfdiscordbot.utils.ConfigLoader;
import ru.itis.mfdiscordbot.utils.IdentityLoader;

@Slf4j
public class DiscordBot implements MasterBot {

    private JDA bot;
    private boolean isActive;
    private final String token;
    private List<IdentityConfig> identities;
    private MessageChannel mainMessageChannel;
    private IdentityLoader identityLoader;

    public DiscordBot(String token) {
        this.token = token;
    }

    @Override
    public void init() throws InitBotException {
        log.info("Initializing discord bot...");
        try {
            bot = JDABuilder.createDefault(token).build();
            identityLoader = new IdentityLoader();
            isActive = false;
        } catch (LoginException e) {
            log.error("Discord bot cannot starts. " + e.getMessage());
            throw new StartBotException("Discord bot cannot starts. " + e.getMessage());
        }
        addEventListeners();
        log.info("Discord bot was initialized");
    }

    @Override
    public void start() throws StartBotException {
        connect();
        setActive(true);
    }

    @Override
    public void stop() throws StopBotException {
        setActive(false);
    }


    private void addEventListeners() {
        log.debug("Adding event handlers...");
        bot.addEventListener(
                new StartBotHandler(this),
                new StopBotHandler(this),
                new HelpBotHandler(this),
                new ReplyHandler(this),
                new ConfigBotHandler(this),
                new RebootConnectionsHandler(this),
                new VersionHandler(this),
                new ConnectionsHandler(this)
                );
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void connect() {
        connect(ConfigLoader.getBotConfigs());
    }

    public void connect(BotConfig[] configs) {
        try{
            log.debug("Connecting to " + Arrays.toString(configs) + "...");
            List<String> tokens = new ArrayList<>();
            for (int i = 0; i < configs.length; i++) {
                tokens.add(configs[i].getToken());
            }
            DiscordBotApp.handleNewConfig(tokens);
            identities = identityLoader.readAll();
            log.debug("Connected");
        } catch (Exception ex){
            sendMessage(ErrorMessage.builder()
                    .text("Невозможно запустить бота в данный момент - проверьте логи.")
                    .build());
            log.error(Arrays.toString(ex.getStackTrace()).replace(",", "\n"));
        }
    }

    public void sendReply(String userId, String message, String botId) {
        log.debug("Trying to send reply to " + userId + ", message " + message + ", botId " + botId);
        for (IdentityConfig config: identities) {
            if (config.getUserId().equals(userId) && config.getBotId().equals(botId)) {
                DiscordBotApp.replyOnMessage(config.getToken(), Reply.builder().userId(userId).message(message).build());
                log.debug("Reply sent");
                return;
            }
        }
        log.warn("Cannot find where to send");
    }


    @Override
    public void sendMessage(Message message) {
        if (this.isActive()){
            if (message instanceof BotMessage){
                log.debug("Received message from server: " + message.toString());
                updateIdentity((BotMessage) message);
                mainMessageChannel.sendMessage(buildTextToAnswer((BotMessage) message)).queue();
            } else if (message instanceof ErrorMessage) {
                mainMessageChannel.sendMessage(((ErrorMessage) message).getText()).queue();
            } else if (message instanceof WarningMessage){
                mainMessageChannel.sendMessage(((WarningMessage) message).getText()).queue();
            } else if (message instanceof NotificationMessage){
                mainMessageChannel.sendMessage(((NotificationMessage) message).getText()).queue();
            }
        }
    }

    private void updateIdentity(BotMessage message) {
        IdentityConfig newIdentity = new IdentityConfig(message.getUserNickname(), message.getToken(), message.getBotName());
        boolean isContains = false;
        for (IdentityConfig identity: identities) {
            if(identity.getUserId().equals(message.getUserNickname()) && identity.getToken().equals(message.getToken()) && identity.getBotId().equals(message.getBotName())) {
                log.debug("Identity already exists");
                isContains = true;
                break;
            }
        }
        if (!isContains) {
            log.debug("Cannot find identity, saving");
            identities.add(newIdentity);
            identityLoader.write(newIdentity);
        }
    }

    // (@BotName) Телеграм: @Username - message
    private String buildTextToAnswer(BotMessage message) {
        StringBuilder text = new StringBuilder("(@" + message.getBotName() + ")");
        switch (message.getMessenger()) {
            case TELEGRAM:
                text.append("Телеграм: ");
        }
        text
          .append("@")
          .append(message.getUserNickname())
          .append(" - ")
          .append(message.getText());
        return text.toString();
    }



    public void setMessageChannel(MessageChannel messageChannel) {
        this.mainMessageChannel = messageChannel;
    }

}
