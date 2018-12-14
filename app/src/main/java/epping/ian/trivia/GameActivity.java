package epping.ian.trivia;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    // global variables
    String playerName;
    Question question;
    int score = 0;

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
        if (savedInstanceState == null) {
            helper.getNextQuestion(this);
        }
        else{
            question = (Question) savedInstanceState.getSerializable("question");
            score = savedInstanceState.getInt("score");
            gotQuestion(question);
        }

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
        outState.putSerializable("question", question);
    }

    public void gotQuestion(Question question) {

        // fill textview with question
        this.question = question;
        TextView questionView = findViewById(R.id.question);
        questionView.setText(question.getQuestion());

        // fill buttons with answers
        TextView answer1 = findViewById(R.id.answer1);
        TextView answer2 = findViewById(R.id.answer2);
        TextView answer3 = findViewById(R.id.answer3);
        TextView answer4 = findViewById(R.id.answer4);

        ArrayList<String> answers = question.getAnswers();
        Collections.shuffle(answers);
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if (user != null) {
            // user is signed in
            String name = user.getDisplayName();
            Log.d("signed in", "onAuthStateChanged:signed_in" + user.getUid());
            }
        else {
            Log.d(" signed out", "onAuthStateChanged:signed_out");
        }
    }

    // if player wants to see previous highscores
    public void onScoreClick(View v) {
        HighScoreHelper newScore = new HighScoreHelper(this);
        newScore.postNewHighScores(highscoreDatabase, playerName, score);
        Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
        intent.putExtra("yourScore", score);
        startActivity(intent);
    }
}