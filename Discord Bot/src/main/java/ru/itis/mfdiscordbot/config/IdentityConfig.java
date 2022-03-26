package ru.itis.mfdiscordbot.config;


public class IdentityConfig {
    private String userId;
    private String token;
    private String botId;

    public IdentityConfig() {
    }

    public IdentityConfig(String userId, String token, String botId) {
        this.userId = userId;
        this.token = token;
        this.botId = botId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    @Override
    public String toString() {
        return "IdentityConfig{" +
          "userId='" + userId + '\'' +
          ", token='" + token + '\'' +
          ", botId='" + botId + '\'' +
          '}';
    }
}
