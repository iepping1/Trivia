package epping.ian.trivia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    String playerName;
    Question question;
    int score = 0;
    List questions;

    // set up firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference highscoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get name of player
        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");

        // get questions from site
        TriviaHelper helper = new TriviaHelper(this);
        helper.getNextQuestion(this);

        // intialize and reference to database
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        highscoreDatabase = database.getReference();
    }

    // save score and settings for restoration
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int storedScore = score;
        outState.putInt("value", storedScore);
    }

    // retrieve score and settings after rotation or leaving app
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int retrievedScore = savedInstanceState.getInt("value");
        score = retrievedScore;
    }

    public void gotQuestion(Question question) {

        // fill textview with question
        this.question = question;
        TextView questionView = findViewById(R.id.question);
        questionView.setText(question.getQuestion());

        // set number of choices
        int multipleChoice = 4;

        // fill answer buttons
        TextView answer1 = findViewById(R.id.answer1);
        TextView answer2 = findViewById(R.id.answer2);
        TextView answer3 = findViewById(R.id.answer3);
        TextView answer4 = findViewById(R.id.answer4);

        questions = question.answers;
        for (int i = 0; i < multipleChoice; i++) {
            Random randomNumber = new Random();
            int random = randomNumber.nextInt(questions.size());
            String currentQuestion = (String) questions.get(random);

            // TODO: Smooth out code here
            questions.remove(random);
            if (i == 0) {
                answer1.setText(currentQuestion);
            }
            if (i == 1) {
                answer2.setText(currentQuestion);
            }
            if (i == 2) {
                answer3.setText(currentQuestion);
            }
            if (i == 3) {
                answer4.setText(currentQuestion);
            }
        }
    }

    public void gotError(String message) {
        // send a message if error has occured
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // response to given answers
    public void answerClicked (View view) {
        String correctAnswer = question.getCorrectAnswer();
        Button button = (Button) view;
        String answer = button.getText().toString();

        // add to score when answer is correct
        if (answer.equals(correctAnswer)){
            score ++;
        }

        // otherwise, end quiz and switch to highscore window
        else {
            HighScoreHelper newScore = new HighScoreHelper(this);
            newScore.postNewHighScores(highscoreDatabase, playerName, score);
            Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
            intent.putExtra("playerName", playerName);
            startActivity(intent);
        }

        // get next question
        TriviaHelper helper = new TriviaHelper(this);
        helper.getNextQuestion(this);
    }

    // check to see if user is signed in and update interface
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if (user != null) {
            //user is signed in
            String name = user.getDisplayName();
            Log.d("signed in", "onAuthStateChanged:signed_in" + user.getUid());
            }
        else {
            Log.d(" signed out", "onAuthStateChanged:signed_out");
            //Intent main = new Intent(GameActivity.this, MainActivity.class);
            //startActivity(main);
        }
    }

    //public void createUser() {
    //}

    // if player wants to see all highscores
    public void onScoreClick(View v) {
        HighScoreHelper newScore = new HighScoreHelper(this);
        newScore.postNewHighScores(highscoreDatabase, playerName, score);
        Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
        intent.putExtra("yourScore", score);
        startActivity(intent);
    }
}