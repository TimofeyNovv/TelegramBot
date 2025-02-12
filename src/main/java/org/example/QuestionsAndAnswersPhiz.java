package org.example;

import java.util.HashMap;
import java.util.Map;

public class QuestionsAndAnswersPhiz {
    private Map<String, String> questionsAndAnswers = new HashMap<>();

    public QuestionsAndAnswersPhiz() {
        questionsAndAnswers.put("1+3", "4");
        questionsAndAnswers.put("При взвешивании груза в воздухе показание динамометра равно 2 Н. При опускании груза в воду показание динамометра" +
                " уменьшается до 1,6 Н. Какова выталкивающая сила, действующая на груз в воде?", "0,4");
       /* questionsAndAnswers.put("", "");
        questionsAndAnswers.put("", "");*/
    }

    public Map<String, String> getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }
}
