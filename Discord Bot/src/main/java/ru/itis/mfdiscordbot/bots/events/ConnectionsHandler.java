package ru.itis.mfdiscordbot.bots.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.DiscordBotApp;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ConnectionsHandler extends ListenerAdapter {

    protected List<String> connectionsCommands;
    protected DiscordBot bot;

    public ConnectionsHandler(DiscordBot bot){
        connectionsCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.connections-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (connectionsCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            if (bot.isActive()){
                StringBuilder answer = new StringBuilder("Текущие активные токены:\n");
                for (String token : DiscordBotApp.getActiveTokens()){
                    answer.append("\t").append(token).append("\n");
                }
                if (DiscordBotApp.getActiveTokens().size()>0){
                    answer.delete(answer.length()-1, answer.length());

                } else {
                    answer.append("\tПока что здесь пусто(");
                }
                event.getChannel().sendMessage(answer.toString()).queue();
            } else {
                event.getChannel().sendMessage("Бот сейчас приостановлен").queue();
            }
        }
    }

}
