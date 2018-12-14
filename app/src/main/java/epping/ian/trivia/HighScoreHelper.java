package epping.ian.trivia;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HighScoreHelper{

    // global context and callback
    Context context;
    Callback callback;

    // list for all scores from database
    ArrayList<HighScore> highScores = new ArrayList<>();

    // constructed highscore from database
    HighScore value;
    String playerName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference highscoreDatabase = database.getReference();

    // constructor for context parameter
    public HighScoreHelper(Context context) {
        this.context = context;
    }

    // call methods for error and succesful requests
    public interface Callback {
        void gotHighScores(ArrayList<HighScore> highscores);
        void gotError(String message);
    }

    // post highscores in database
    void postNewHighScores (final DatabaseReference highscoreDatabase, final String playerName, final int score){

        HighScore newScore = new HighScore(playerName, score);
        highscoreDatabase.child("highscores").child("score of " + playerName).setValue(newScore);
    }

    // retrieve highscores from game
    public void getHighScores(final Callback delegate, final DatabaseReference highscoreDatabase) {

        callback = delegate;
        DatabaseReference reference = highscoreDatabase.child("highscores");

        reference.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // run through datesnashots
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    String name = childSnapshot.child("name").getValue(String.class);
                    int score = childSnapshot.child("score").getValue(Integer.class);
                    value = new HighScore(name, score);
                    highScores.add(value);
                }
                // sort the scores from low to high
                Collections.reverse(highScores);

                delegate.gotHighScores(highScores);
            }

            // handle errors
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("onCancelled", "Failed to read High Score", databaseError.toException());
                delegate.gotError("databaseError");
            }
        });
    }
}