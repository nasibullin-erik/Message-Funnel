package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

public class HelpBotHandler extends ListenerAdapter {

    protected List<String> helpCommands;
    protected DiscordBot bot;

    public HelpBotHandler(DiscordBot bot){
        helpCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.help-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (helpCommands.contains(event.getMessage().getContentRaw().toLowerCase())){
            event.getChannel().sendMessage("(/start | /старт) - запустить бота\n" +
                    "(/stop | /стоп) - приостановить работу бота\n" +
                    "(/config | /конфиг) + YAML файл - сконфигурировать зависимых ботов\n" +
                    "\tВажно отправлять файл вместе с командой (т.е. прикрепить файл и написать к нему комментарий-команду /config)\n" +
                    "\tФайл должен содержать все токены, к которым Вы хотите подключиться\n" +
                    "\tПервый символ знак минус, далее через пробел запись вида token: 1234, где 1234 - токен бота\n" +
                    "\tПример конфиг-файла:\n" +
                    "\t- token: Cwg3edb66gxSDogtQHZpbNIQpA03212\n" +
                    "\t- token: NzQ0NTE0OTQ4OTE4NTQyMzM24342\n" +
                    "\t- token: XzkVlA54321\n" +
                    "(/config | /конфиг) [(add | добавить) | (remove | удалить)] <token>: \n"  +
                    "\t(add | добавить) - добавление нового токена в конфигурацию\n" +
                    "\t(remove | удалить) - удаление токена из конфигурацию\n" +
                    "\t<token> - значение токена\n" +
                    "(/reboot | /перезагрузить) - обновить соединения бота с существующей конфигурацией (может быть полезно, если одно из них ранее упало)\n" +
                    "(/version | /версия) - посмотреть текущую версию бота\n" +
                    "(/connections | /соединения) - просмотреть все текущие активные соединения."
                    )
                    .queue();
        }
    }

}
