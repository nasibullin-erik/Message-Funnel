package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfbotsapi.api.utils.ErrorMessage;
import ru.itis.mfbotsapi.api.utils.NotificationMessage;
import ru.itis.mfbotsapi.api.utils.WarningMessage;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.config.BotConfig;
import ru.itis.mfdiscordbot.exceptions.ConfigLoaderException;
import ru.itis.mfdiscordbot.exceptions.NoSuchTokenException;
import ru.itis.mfdiscordbot.exceptions.TokenAlreadyExistsException;
import ru.itis.mfdiscordbot.utils.ConfigLoader;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

@Slf4j
public class ConfigBotHandler extends ListenerAdapter {

    protected List<String> configCommands;
    protected List<String> addFlags;
    protected List<String> removeFlags;
    protected DiscordBot bot;

    public ConfigBotHandler(DiscordBot bot){
        configCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-commands").split(", "));
        addFlags = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-commands.add-flags").split(", "));
        removeFlags = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-commands.remove-flags").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split(" ");
        if (configCommands.contains(parts[0].toLowerCase()) && bot.isActive()) {
            if (event.getMessage().getAttachments().size()>0){
                BotConfig[] configs = ConfigLoader.getBotConfigs(event.getMessage().getAttachments().get(0));
                if (checkConfig(configs)) {
                    bot.connect(configs);
                } else {
                    event.getChannel().sendMessage("Неверный или пустой конфиг файл, напишите /help чтобы узнать подходящий формат").queue();
                }
            } else {
                try{
                    if (addFlags.contains(parts[1].toLowerCase())){
                        addToken(parts[2]);
                    } else if (removeFlags.contains(parts[1].toLowerCase())){
                        removeToken(parts[2]);
                    } else {
                        throw new IndexOutOfBoundsException();
                    }

                } catch (IndexOutOfBoundsException ex){
                    event.getChannel().sendMessage("Неверный формат команды. Напишите /help, чтобы узнать поддерживаемый.").queue();
                }
            }
        }
    }

    protected void addToken(String token){
        try{
            BotConfig botConfig = new BotConfig();
            botConfig.setToken(token);
            ConfigLoader.addNewConfig(botConfig);
            bot.sendMessage(NotificationMessage.builder()
                    .text("Токен был успешно добавлен")
                    .build());
            DiscordBotApp.establishNewConnection(token);
        } catch (ConfigLoaderException ex){
            bot.sendMessage(ErrorMessage.builder()
                    .text("Не удалось добавить новый токен :С")
                    .build());
        } catch (TokenAlreadyExistsException ex){
            bot.sendMessage(WarningMessage.builder()
                    .text("Данный токен уже существует.")
                    .build());
        }
    }

    protected void removeToken(String token){
        try{
            BotConfig botConfig = new BotConfig();
            botConfig.setToken(token);
            ConfigLoader.removeConfig(botConfig);
            bot.sendMessage(NotificationMessage.builder()
                    .text("Токен был успешно удалён.")
                    .build());
            DiscordBotApp.obliterateConnection(token);
        } catch (ConfigLoaderException ex){
            bot.sendMessage(ErrorMessage.builder()
                    .text("Не удалось добавить удалить токен :С")
                    .build());
        } catch (NoSuchTokenException ex){
            bot.sendMessage(WarningMessage.builder()
                    .text("Данного токена нет в конфигурации.")
                    .build());
        }
    }

    protected boolean checkConfig(BotConfig[] config) {
        return config.length != 0;
    }
}
