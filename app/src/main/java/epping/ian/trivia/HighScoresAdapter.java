package epping.ian.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoresAdapter extends ArrayAdapter<HighScore> {

    private ArrayList<HighScore> highscores;

    public HighScoresAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HighScore> objects) {
        super(context, resource, objects);
        highscores = objects;
    }

    // create fields for adapterview
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(getContext());
            convertView = inf.inflate(R.layout.high_score, parent, false);
        }

        HighScore highScore = highscores.get(position);

        String nameInput = highScore.getName();
        String scoreInput = Integer.toString(highScore.getScore());

        // get references to item fields
        TextView named = convertView.findViewById(R.id.name);
        TextView scored = convertView.findViewById(R.id.score);

        // show name and score
        named.setText(nameInput);
        scored.setText(scoreInput);

        return convertView;
    }
}
