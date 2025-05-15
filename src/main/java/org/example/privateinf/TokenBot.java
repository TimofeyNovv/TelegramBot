package org.example.privateinf;

import io.github.cdimascio.dotenv.Dotenv;

public class TokenBot {
    Dotenv dotenv = Dotenv.load();

    private String BotToken = dotenv.get("BOT_TKN");
    private String BotUsername = dotenv.get("BOT_NM");

    public String getBotUsername() {
        return BotUsername;
    }

    public String getBotToken() {
        return BotToken;
    }
}
