package epping.ian.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity implements HighScoreHelper.Callback{

    private FirebaseAuth mAuth;
    public DatabaseReference highscoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // initialize the firebase instance
        mAuth = FirebaseAuth.getInstance();

        // write to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        highscoreDatabase = database.getReference();

        // get highscores
        HighScoreHelper scoreHelper = new HighScoreHelper(this);
        scoreHelper.getHighScores(this, highscoreDatabase);
    }

    public void gotHighScores(ArrayList<HighScore> highscores) {

        // connect adapter and fill listview with data
        HighScoresAdapter adapter = new HighScoresAdapter(this, R.layout.high_score, highscores);
        ListView ScoreList = findViewById(R.id.listScores);
        ScoreList.setAdapter(adapter);
    }

    public void gotError(String message) {

        // send message if error has occured
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // if player wants to retry game
    public void onResetClick(View v) {
        Intent intent = new Intent(HighScoreActivity.this, GameActivity.class);
        startActivity(intent);
    }

    // if player wants to end game
    public void onResetClick2(View v) {
        Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
        startActivity(intent);
    }
}