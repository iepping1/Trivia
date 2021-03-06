package epping.ian.trivia;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    // create class for all game fields
    public String question, correctAnswer;
    public ArrayList<String> answers;

    //store game fields
    public Question (String question, ArrayList<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    // retrieve fields
    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }

    public ArrayList<String> getAnswers() { return answers; }

    public void setAnswers(ArrayList<String> answers) { this.answers = answers; }

    public String getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
