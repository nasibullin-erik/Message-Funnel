package ru.itis.mfbotsapi.api.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotMessage implements Message {

    protected String token;
    protected String userId;
    protected String userNickname;
    protected String text;
    protected String botName;
    protected SlaveBotEntry.Messenger messenger;

}
