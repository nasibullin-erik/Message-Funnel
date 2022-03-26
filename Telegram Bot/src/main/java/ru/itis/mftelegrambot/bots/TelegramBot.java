package ru.itis.mftelegrambot.bots;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.itis.mfbotsapi.api.utils.Reply;
import ru.itis.mfbotsapi.bots.SlaveBot;
import ru.itis.mfbotsapi.bots.exceptions.StartBotException;
import ru.itis.mftelegrambot.TelegramBotApp;
import ru.itis.mftelegrambot.utils.PropertiesConstants;
import ru.itis.mftelegrambot.utils.PropertiesLoader;

public class TelegramBot extends TelegramLongPollingBot implements SlaveBot {
    private Logger logger;

    private String name;
    private String token;

    private String usernameTG;

    private Map<String, Long> mesMap;

    public TelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
        mesMap = new HashMap<>();
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getFrom().getUserName() + " прислал: " + update.getMessage().getText());
        if(update.getMessage().getText().equals("/start")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            sendMessage.setText("Hello, my dear friend! \n The bot forwards all discord messages to Message_Funnel_Bot");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }
        else if(update.getMessage().getText().equals("/token")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            sendMessage.setText(PropertiesLoader.getInstance().getProperty(PropertiesConstants.TELEGRAM_BOT_LTOKEN));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }
        else if(update.getMessage().getText().equals("/version")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            sendMessage.setText(PropertiesLoader.getInstance().getProperty(PropertiesConstants.TELEGRAM_BOT_VERSION));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }
        else {
            String userId = update.getMessage().getFrom().getId().toString();
            String userName = update.getMessage().getFrom().getUserName().toString();
            String text = update.getMessage().getText();
            mesMap.put(userName, update.getMessage().getChatId());
            TelegramBotApp.sendMessage(userId, userName, text);
        }
    }

    @Override
    public void start() throws StartBotException {
        logger = Logger.getLogger(this.getClass().getName());
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            logger.log(Level.INFO ,"...Telegram bot was started...^_^...");

        } catch (TelegramApiException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new StartBotException("Telegram bot cannot starts. " + e.getMessage());
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void init() {

    }

//    public synchronized void sendMsg(String chatId, String s, int mesId, String usernameTG, int repMesId) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.enableMarkdown(true);
//        sendMessage.setChatId(chatId);
//        sendMessage.setReplyToMessageId(mesId);
//        sendMessage.setText("Message from tg " + "\n"+ s + "\n" + "by " + usernameTG + "\n repMesId: " + repMesId);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            logger.log(Level.ERROR, e.toString());
//        }
//    }

    @Override
    public void sendReply(Reply reply) {
        SendMessage sendMes = new SendMessage();
        sendMes.setChatId(String.valueOf(mesMap.get(reply.getUserId())));
        sendMes.setText(reply.getMessage());
        try {
            execute(sendMes);
        } catch (TelegramApiException e) {
            logger.log(Level.ERROR, e.toString());
        }
    }

    @Override
    public String getBotName() {
        return name;
    }
}
