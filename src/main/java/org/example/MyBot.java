package org.example;

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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    private TokenBot tokenBot = new TokenBot();

    //переменная, хранящая правильный ответ
    private String trueotvet = "";

    //Создаю Hashmap для проверки на соответствие в ней будет содержатьмся правильный ответ
    private Map<Long, String> expectedAnswers = new HashMap<>();

    //создаю объект класса для вопросов по математике
    QuestionsAndAnswersMath questionsAndAnswersMath = new QuestionsAndAnswersMath();

    //Заполняю HashMap вопросами и ответами  на них
    private Map<String, String> questionsAndAnswersMathMap = questionsAndAnswersMath.getQuestionsAndAnswers();

    //Создаю объект класса для вопросов по информатике
    QuestionsAndAnswersInf questionsAndAnswersInf = new QuestionsAndAnswersInf();

    //Заполняю HashMap вопросами и ответами на них
    private Map<String, String> questionsAndAnswersInfMap = questionsAndAnswersInf.getQuestionsAndAnswers();

    //Создаю объект класса для вопросов по физике
    QuestionsAndAnswersPhiz questionsAndAnswersPhiz = new QuestionsAndAnswersPhiz();

    //Заполняю HashMap вопросами и ответами на них
    private Map<String, String> getQuestionsAndAnswersPhizMap = questionsAndAnswersPhiz.getQuestionsAndAnswers();

    //Создаю переменную для хранения изображение которое буду высылать
    private String imageVoprPhiz = "images/imagesPhiz/svecha1.jpg";


    //метод для создания сообщения
    private SendMessage createMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }
    //метод для создания посылаемого фото
    /*private SendPhoto createPhoto(){

    }*/

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

    //Метод для определения картинки которую надо будет послать
    private String imageVoprPhiz(String answer) {
        System.out.println(answer);
        String str = "images/imagesPhiz/svecha1.jpg";
        if (answer.equals("При взвешивании груза в воздухе показание динамометра равно 2 Н. При опускании груза в воду показание динамометра" +
                " уменьшается до 1,6 Н. Какова выталкивающая сила, действующая на груз в воде?")) {
            str = "images/imagesPhiz/svecha1.jpg";
        } else str = null;

        return str;
    }

    //Метод для создания кнопок
    public void ReplKeyBMark(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        //добавление кнопок
        row.add("Информация");
        row.add("Вопросы из ОГЭ по математике");
        row.add("Вопросы из ОГЭ по физике");
        row.add("Вопросы из ОГЭ по информатике");
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(replyKeyboardMarkup);
    }

    //Метод для обработки вводимых команд
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            //Действия если сообщение будет равно /start
            if (messageText.equals("/start")) {
                expectedAnswers.put(chatId, null);
                SendMessage startMessage = createMessage(chatId, "Привет! Нажми на кнопку, чтобы получить сообщение.");
                ReplKeyBMark(startMessage);
                try {
                    execute(startMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Действия если сообщение будет равно Информация(т.е. нажата кнопка информация)
            else if (messageText.equals("Информация")) {
                SendMessage responseMessage = createMessage(chatId, "Ответы пишите по правилам заполнения первого бланка ответов ОГЭ.");
                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Действия если сообщение будет равно Вопросы из ОГЭ по математике(т.е. нажата кнопка Вопросы из ОГЭ по математике)
            else if (messageText.equals("Вопросы из ОГЭ по математике")) {
                String randomQuestion = getRandomKey(questionsAndAnswersMathMap);
                expectedAnswers.put(chatId, questionsAndAnswersMathMap.get(randomQuestion));
                trueotvet = questionsAndAnswersMathMap.get(randomQuestion);
                SendMessage responseMessage = createMessage(chatId, randomQuestion);

                try {
                    execute(responseMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Действия если сообщение будет равно Вопросы из ОГЭ по информатике(т.е. нажата кнопка Вопросы из ОГЭ по информатике)
            else if (messageText.equals("Вопросы из ОГЭ по информатике")) {
                String randomQuestion = getRandomKey(questionsAndAnswersInfMap);
                expectedAnswers.put(chatId, questionsAndAnswersInfMap.get(randomQuestion));
                trueotvet = questionsAndAnswersInfMap.get(randomQuestion);
                SendMessage infMassege = createMessage(chatId, randomQuestion);
                try {
                    execute(infMassege);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Действия если сообщение будет равно Вопросы из ОГЭ по физике(т.е. нажата кнопка Вопросы из ОГЭ по физике)
            else if (messageText.equals("Вопросы из ОГЭ по физике")) {
                String randomQuestion = getRandomKey(getQuestionsAndAnswersPhizMap);
                expectedAnswers.put(chatId, getQuestionsAndAnswersPhizMap.get(randomQuestion));
                trueotvet = getQuestionsAndAnswersPhizMap.get(randomQuestion);
                SendMessage pfizMassege = createMessage(chatId, randomQuestion);
                imageVoprPhiz = imageVoprPhiz(expectedAnswers.get(chatId));
                System.out.println(imageVoprPhiz);

                /*if (imageVoprPhiz != null) {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(String.valueOf(chatId));
                    InputFile photo = new InputFile(new File(imageVoprPhiz));
                    sendPhoto.setPhoto(photo);
                    sendPhoto.setCaption(randomQuestion);
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else {}*/
                    try {
                        execute(pfizMassege);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
            //Если введённое пользователем равно ответ
            else if (messageText.toLowerCase().equals("ответ") && expectedAnswers.get(chatId) != null) {
                SendMessage otvetMessage = createMessage(chatId, "Правильный ответ - " + trueotvet);
                ReplKeyBMark(otvetMessage);
                try {
                    execute(otvetMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Проверка ответа пользователя
            else if (expectedAnswers.get(chatId) != null) {
                //Ожидаемый ответ
                String expectedAnswer = expectedAnswers.get(chatId);
                SendMessage message = createMessage(chatId, ".");
                if (expectedAnswer != null && messageText.equalsIgnoreCase(expectedAnswer)) {
                    message = createMessage(chatId, "Правильно.");
                    expectedAnswers.remove(chatId);
                    ReplKeyBMark(message);
                    expectedAnswers.put(chatId, null);
                } else {
                    message = createMessage(chatId, "Неправильно, если хотите узнать правильный ответ, напишите ответ.");

                }
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //Если пользователь ввёл не команду
            else {
                SendMessage messageEsliNepravVvod = createMessage(chatId, "Пожалуйста введите команду /start или нажмите на кнопки.");
                ReplKeyBMark(messageEsliNepravVvod);
                try {
                    execute(messageEsliNepravVvod);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //Метод для получения случайного ключа т.е. для получения случайного вопроса
    private String getRandomKey(Map<String, String> map) {
        Random random = new Random();
        int randomIndex = random.nextInt(map.size());
        return map.keySet().toArray(new String[0])[randomIndex];
    }
    //MAIN 
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
