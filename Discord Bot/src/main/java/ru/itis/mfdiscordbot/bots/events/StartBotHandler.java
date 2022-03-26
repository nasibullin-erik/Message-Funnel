package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

public class StartBotHandler extends ListenerAdapter {

    protected List<String> startCommands;
    protected String welcomeMessage;
    protected DiscordBot bot;

    public StartBotHandler(DiscordBot bot){
        startCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.start-commands").split(", "));
        welcomeMessage = PropertiesLoader.getInstance().getProperty("discord.bot.welcome-message");
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (startCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (!bot.isActive()){
                if (welcomeMessage != null){
                    event.getChannel().sendMessage(welcomeMessage).queue();
                }
                bot.setMessageChannel(event.getChannel());
                bot.start();
            } else {
                event.getChannel().sendMessage("Бот уже работает").queue();
            }
        }
    }

}
