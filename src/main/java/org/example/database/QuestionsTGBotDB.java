package org.example.database;

import org.example.privateinf.PrivateInfDB;

import java.sql.*;
import java.util.ArrayList;

public class QuestionsTGBotDB {
    PrivateInfDB privateInfDB = new PrivateInfDB();

    Connection connection = null;
    Statement statement = null;


    public QuestionsTGBotDB() {
        try {
            Class.forName("org.postgresql.Driver");
            //Осуществляю связь с БД
            connection = DriverManager
                    .getConnection(privateInfDB.getUrl(), privateInfDB.getName(), privateInfDB.getPassword());
            //Для реализации запроса
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Метод для получения массива содержащего вопрос ответ и картинку по физике
    public ArrayList<String> getVoprosOtveImageInformationPhiz(int uid) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM informationphiz WHERE uid = " + uid);
            while (resultSet.next()) {
                arrayList.add(resultSet.getString("vopros"));
                arrayList.add(resultSet.getString("otvet"));
                arrayList.add(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    //Метод для получения массива содержащего вопрос ответ и картинку по математике
    public ArrayList<String> getVoprosOtveImageInformationMath(int uid) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM informationmath WHERE uid = " + uid);
            while (resultSet.next()) {
                arrayList.add(resultSet.getString("vopros"));
                arrayList.add(resultSet.getString("otvet"));
                arrayList.add(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }
    //Метод для получения массива содержащего вопрос ответ и картинку по информатике
    public ArrayList<String> getVoprosOtveImageInformationInf(int uid) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM informationinf WHERE uid = " + uid);
            while (resultSet.next()) {
                arrayList.add(resultSet.getString("vopros"));
                arrayList.add(resultSet.getString("otvet"));
                arrayList.add(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    //Метод для получения количества столбцов из таблички с вопросами по математике
    public int getColvoStolbtcovMath() {
        int colvo = 0;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(uid) FROM informationmath");
            while (resultSet.next()){
                colvo = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return colvo;
    }

    //Метод для получения количества столбцов из таблички с вопросами по физике
    public int getColvoStolbtcovPhiz() {
        int colvo = 0;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(uid) FROM informationphiz");
            while (resultSet.next()){
                colvo = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return colvo;
    }
    //Метод для получения количества столбцов из таблички с вопросами по физике
    public int getColvoStolbtcovInf() {
        int colvo = 0;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(uid) FROM informationinf");
            while (resultSet.next()){
                colvo = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return colvo;
    }


    //Метод для завершения работы с БД
    public void finish() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
