package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

public class StopBotHandler extends ListenerAdapter {

    protected List<String> stopCommands;
    protected DiscordBot bot;

    public StopBotHandler(DiscordBot bot){
        stopCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.stop-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (stopCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (bot.isActive()){
                bot.stop();
                event.getChannel().sendMessage("Бот приостановлен.").queue();
            } else {
                event.getChannel().sendMessage("Бот уже приостановлен.").queue();
            }
        }
    }


}
