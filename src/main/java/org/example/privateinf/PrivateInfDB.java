package org.example.privateinf;

public class PrivateInfDB {
    String url = "jdbc:postgresql://localhost:5432/questionsTGBot";
    String name = "postgres";
    String password = "timn2020";

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
