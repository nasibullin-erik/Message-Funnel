package ru.itis.mfdiscordbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import ru.itis.mfdiscordbot.config.BotConfig;
import ru.itis.mfdiscordbot.config.IdentityConfig;
import ru.itis.mfdiscordbot.utils.IdentityLoader;

public class Test {
    public static void main(String[] args) throws Exception{
        String text = "(@Message_Funnel_bot)Телеграм: @Erik_Nasibullin - test";
        int first = text.indexOf(':') + 2;
        int second = first + 2;
        while(text.charAt(second) != ' ') {
            second++;
        }
        System.out.println(text.substring(first, second));
    }
}
