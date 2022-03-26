package ru.itis.mfdiscordbot.config;


import java.util.Objects;

public class BotConfig {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "BotConfig{" +
          "token='" + token + '\'' +
          '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotConfig botConfig = (BotConfig) o;
        return Objects.equals(token, botConfig.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
