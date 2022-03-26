package ru.itis.mfdiscordbot.bots.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class VersionHandler extends ListenerAdapter {

    protected List<String> versionCommands;
    protected DiscordBot bot;
    protected String version;

    public VersionHandler(DiscordBot bot){
        versionCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.version-commands").split(", "));
        version= PropertiesLoader.getInstance().getProperty("discord.bot.version");
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (versionCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            event.getChannel().sendMessage(version).queue();
        }
    }


}
