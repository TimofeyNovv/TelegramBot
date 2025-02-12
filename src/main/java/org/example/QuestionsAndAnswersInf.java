package org.example;

import java.util.HashMap;
import java.util.Map;

public class QuestionsAndAnswersInf {
    private Map<String,String> questionsAndAnswers= new HashMap<>();

    public QuestionsAndAnswersInf(){
        questionsAndAnswers.put("1+1","2");
    }

    public Map<String, String> getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }
}
