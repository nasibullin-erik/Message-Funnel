package ru.itis.mfdiscordbot.bots.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RebootConnectionsHandler extends ListenerAdapter {

    protected List<String> rebootCommands;
    protected DiscordBot bot;

    public RebootConnectionsHandler(DiscordBot bot){
        rebootCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.reboot-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (rebootCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (bot.isActive()){
                event.getChannel().sendMessage("Бот перезапущен.").queue();
                bot.connect();
            } else {
                event.getChannel().sendMessage("Бот приостановлен").queue();
            }
        }
    }

}
