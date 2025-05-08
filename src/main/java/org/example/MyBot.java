package org.example;

import org.example.database.QuestionsTGBotDB;
import org.example.privateinf.TokenBot;
import org.example.view.Button;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    //Переменная, хранящая, отправляемое сообщение
    private SendMessage otpravMessage = null;

    private Button button;

    //Переменная, хранящая, отправляемое сообщение
    private SendPhoto otpravPhoto = null;

    //Создаю экземпляр класса QuestionsTGBot
    private QuestionsTGBotDB questionsTGBot = new QuestionsTGBotDB();

    //Создаю экземпляр класса чтобы в будущем получать токен и имя бота
    private TokenBot tokenBot = new TokenBot();


    //Создаю Hashmap для проверки на соответствие в ней будет содержаться правильный ответ
    private Map<Long, String> expectedAnswers = new HashMap<>();


    //метод для создания сообщения
    private SendMessage createMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }
    //Метод для получения случайного uid
    public int getRandomUid(int conech){
        Random random = new Random();
        return random.nextInt(conech) + 1;
    }

    //геттер для имени бота
    @Override
    public String getBotUsername() {
        return tokenBot.getBotUsername();
    }

    //геттер для токена бота
    @Override
    public String getBotToken() {
        return tokenBot.getBotToken();
    }


//--------------------------------Метод для обработки вводимых команд--------------------------------------------------------
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

//---------------------------------Действия если сообщение будет равно /start--------------------------------------------
            if (messageText.equals("/start")) {
                expectedAnswers.put(chatId, null);
                otpravMessage =  createMessage(chatId, "Привет! Нажми на кнопку, чтобы получить сообщение.");
                button.ReplKeyBMark(otpravMessage);
                try {
                    execute(otpravMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
//-----------------------------------Действия если сообщение будет равно Информация(т.е. нажата кнопка информация)------------------------
            else if (messageText.equals("Информация")) {
                otpravMessage = createMessage(chatId, "Ответы пишите по правилам заполнения первого бланка ответов ОГЭ.");
                try {
                    execute(otpravMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
//-----------------------------------Действия если сообщение будет равно Вопросы из ОГЭ по математике(т.е. нажата кнопка Вопросы из ОГЭ по математике)---------------------------------
            else if (messageText.equals("Вопросы из ОГЭ по математике")) {
                //Создаю массив, который будет хранить вопрос ответ и картинку
                ArrayList<String> questionsMathArray = new ArrayList<>(questionsTGBot.getVoprosOtveImageInformationMath(getRandomUid(questionsTGBot.getColvoStolbtcovMath())));
                //Проверяю есть ли картинка
                if (questionsMathArray.get(2) != null) {
                    //Записываю URL картинки в переменную
                    String imageURL = questionsMathArray.get(2);
                    otpravPhoto = new SendPhoto();
                    otpravPhoto.setChatId(String.valueOf(chatId));
                    otpravPhoto.setPhoto(new InputFile(imageURL));
                    //Прикрепляю текс вопроса к картинке
                    otpravPhoto.setCaption(questionsMathArray.get(0));
                    //Записываю правильный ответ в переменную
                    expectedAnswers.put(chatId, questionsMathArray.get(1));

                } else { //Если картинки нету отправляю вопрос
                    String randomQuestion = questionsMathArray.get(0);
                    expectedAnswers.put(chatId, questionsMathArray.get(1));
                    otpravMessage = createMessage(chatId, randomQuestion);
                }
                try {
                    //Отсылаю сообщение с картинкой или без
                    if (otpravPhoto == null) {
                        execute(otpravMessage);
                        otpravMessage = null;
                    } else {
                        execute(otpravPhoto);
                        otpravPhoto = null;
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
//-----------------------------------Действия если сообщение будет равно Вопросы из ОГЭ по информатике(т.е. нажата кнопка Вопросы из ОГЭ по информатике)--------
            else if (messageText.equals("Вопросы из ОГЭ по информатике")) {
                //Создаю массив, который будет хранить вопрос ответ и картинку
                ArrayList<String> questionsInfArray = new ArrayList<>(questionsTGBot.getVoprosOtveImageInformationInf(getRandomUid(questionsTGBot.getColvoStolbtcovInf())));
                //Проверяю есть ли картинка
                if (questionsInfArray.get(2) != null) {

                    //Записываю URL картинки в переменную
                    String imageURL = questionsInfArray.get(2);
                    otpravPhoto = new SendPhoto();
                    otpravPhoto.setChatId(String.valueOf(chatId));
                    otpravPhoto.setPhoto(new InputFile(imageURL));
                    //Прикрепляю текс вопроса к картинке
                    otpravPhoto.setCaption(questionsInfArray.get(0));
                    //Записываю правильный ответ в переменную
                    expectedAnswers.put(chatId, questionsInfArray.get(1));

                } else { //Если картинки нету отправляю вопрос
                    String randomQuestion = questionsInfArray.get(0);
                    expectedAnswers.put(chatId, questionsInfArray.get(1));
                    otpravMessage = createMessage(chatId, randomQuestion);
                }
                try {
                    //Отсылаю сообщение с картинкой или без
                    if (otpravPhoto == null) {
                        execute(otpravMessage);
                        otpravMessage = null;
                    } else {
                        execute(otpravPhoto);
                        otpravPhoto = null;
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

//-----------------------------------Действия если сообщение будет равно Вопросы из ОГЭ по физике(т.е. нажата кнопка Вопросы из ОГЭ по физике)---------------------
            else if (messageText.equals("Вопросы из ОГЭ по физике")) {
                //Создаю массив, который будет хранить вопрос ответ и картинку
                ArrayList<String> questionsPhizArray = new ArrayList<>(questionsTGBot.getVoprosOtveImageInformationPhiz(getRandomUid(questionsTGBot.getColvoStolbtcovPhiz())));
                //Проверяю есть ли картинка
                if (questionsPhizArray.get(2) != null) {
                    //Записываю URL картинки в переменную
                    String imageURL = questionsPhizArray.get(2);
                    otpravPhoto = new SendPhoto();
                    otpravPhoto.setChatId(String.valueOf(chatId));
                    otpravPhoto.setPhoto(new InputFile(imageURL));
                    //Прикрепляю текс вопроса к картинке
                    otpravPhoto.setCaption(questionsPhizArray.get(0));
                    //Записываю правильный ответ в переменную
                    expectedAnswers.put(chatId, questionsPhizArray.get(1));

                } else { //Если картинки нету отправляю вопрос
                    String randomQuestion = questionsPhizArray.get(0);
                    expectedAnswers.put(chatId, questionsPhizArray.get(1));
                    otpravMessage = createMessage(chatId, randomQuestion);
                }
                try {
                    //Отсылаю сообщение с картинкой или без
                    if (otpravPhoto == null) {
                        execute(otpravMessage);
                        otpravMessage  = null;
                    } else {
                        execute(otpravPhoto);
                        otpravPhoto = null;
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }


//-----------------------------------Если введённое пользователем равно ответ-------------------------------------------
            else if (messageText.toLowerCase().equals("ответ") && expectedAnswers.get(chatId) != null) {
                otpravMessage = createMessage(chatId, "Правильный ответ - " + expectedAnswers.get(chatId));
                button.ReplKeyBMark(otpravMessage);
                try {
                    execute(otpravMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
//-------------------------------------Проверка ответа пользователя---------------------------------------------------
            else if (expectedAnswers.get(chatId) != null) {
                //Ожидаемый ответ
                String expectedAnswerOzhid = expectedAnswers.get(chatId);
                //SendMessage message = createMessage(chatId, ".");
                if (expectedAnswerOzhid != null && messageText.equalsIgnoreCase(expectedAnswerOzhid)) {
                    otpravMessage = createMessage(chatId, "Правильно.");
                    expectedAnswers.remove(chatId);
                    button.ReplKeyBMark(otpravMessage);
                    expectedAnswers.put(chatId, null);
                } else {
                    otpravMessage = createMessage(chatId, "Неправильно, если хотите узнать правильный ответ, напишите ответ.");

                }
                try {
                    execute(otpravMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
//-----------------------------------Если пользователь ввёл не команду---------------------------------------------------
            else {
                otpravMessage = createMessage(chatId, "Пожалуйста введите команду /start или нажмите на кнопки.");
                button.ReplKeyBMark(otpravMessage);
                try {
                    execute(otpravMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }

//---------------------------------------------MAIN--------------------------------------------------------------------
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}