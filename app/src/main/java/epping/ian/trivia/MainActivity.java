package epping.ian.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// save questions until answered when page reloads < change oncreate

// intro screen
public class MainActivity extends AppCompatActivity {

    // Set up playername
    String playerName;
    EditText getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get name from user
        getName = findViewById(R.id.getName);
    }

    public void GameClicked (View view){

        // check if name was entered
        if (getName.getText().toString().equals("")){

            // ask player for name if not
            Toast toast = Toast.makeText(this, " Please enter a name", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            // set up playername and start the game
            playerName = getName.getText().toString();

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("playerName", playerName);
            startActivity(intent);
        }
    }
}
