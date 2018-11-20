package epping.ian.trivia;

import java.io.Serializable;

public class HighScore implements Serializable {

    // create class for all highscore fields
    public String name;
    public int score;

    // argument constructor for the database
    public HighScore() {}

    // store the fields
    public HighScore(String name, int score) {
        this.name = name;
        this.score= score;
    }

    // retrieve the fields
    public String getName() { return name; }

    public Integer getScore() { return score; }

    public void setName(String name) { this.name = name; }

    public void setScore(int score) { this.score = score; }
}
