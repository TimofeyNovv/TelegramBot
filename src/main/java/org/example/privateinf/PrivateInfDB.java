package org.example.privateinf;

import io.github.cdimascio.dotenv.Dotenv;

public class PrivateInfDB {
    Dotenv dotenv = Dotenv.load();
    String url = dotenv.get("URL_DB");
    String name = dotenv.get("DB_NM");
    String password = dotenv.get("DB_PS");

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
