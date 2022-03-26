package ru.itis.mfdiscordbot.bots.events;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;

public class ReplyHandler extends ListenerAdapter {

    private final DiscordBot bot;

    public ReplyHandler(DiscordBot bot) {
        this.bot = bot;
    }

    //(@Message_Funnel_bot2)Телеграм: @Koala1101 - /start
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message referencedMessage = event.getMessage().getReferencedMessage();
        if (bot.isActive()) {
            if (referencedMessage != null && referencedMessage.getAuthor().isBot()) {
                String text = referencedMessage.getContentRaw();
                String userId = findUserId(text);
                String botId = findBotId(text);

                bot.sendReply(userId, event.getMessage().getContentRaw(), botId);
            } else {
                if (!event.getMessage().getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith("/")){
                    event.getMessage().getChannel().sendMessage("Не знаем кому отправлять").queue();
                }
            }
        }
    }

    private String findUserId(String text) {
        int startId = text.indexOf(':') + 3;
        int endId = startId + 2;
        while(text.charAt(endId) != ' ') {
            endId++;
        }
        return text.substring(startId, endId);
    }


    private String findBotId(String text) {
        int startId = 2;
        int endId = startId + 2;
        while(text.charAt(endId) != ')') {
            endId++;
        }
        return text.substring(startId, endId);
    }
}
