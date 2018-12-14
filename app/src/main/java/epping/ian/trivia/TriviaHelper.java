package epping.ian.trivia;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class TriviaHelper implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Context context;
    private Callback callback;

    // call methods for failed and succesful requests
    public interface Callback {
        void gotQuestion(Question question);
        void gotError(String message);
    }

    // constructor for context parameter
    public TriviaHelper(Context context) {
        this.context = context;
    }

    // retrieves questions from API
    void getNextQuestion(Callback callback){
        this.callback = callback;

        // link to the API site
        String url = "http://jservice.io/api/category?id=267";

        // request data from API
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(url, null, this, this);
        queue.add(request);
    }

    // listener for succesful requests
    public void onResponse(JSONObject response) {

        try {
            // retrieve answers
            JSONArray array = response.getJSONArray("clues");
            ArrayList<String> answers = new ArrayList<>();
            Random randomNumber = new Random();
            int random = randomNumber.nextInt(array.length());

            // fill question array with random answers
            for (int i = 1; i < 4; i++) {
                JSONObject nextQuestion = array.getJSONObject(random);
                String answer = nextQuestion.getString("answer");
                answers.add(answer);
                array.remove(random);
            }

            // retrieve proper questions
            JSONObject thisQuestion = array.getJSONObject(random);
            String correctQuestion = thisQuestion.getString("question");
            String correctAnswer = thisQuestion.getString("answer");
            answers.add(correctAnswer);

            // pass all to game activity
            Question lastQuestion = new Question(correctQuestion, answers, correctAnswer);
            callback.gotQuestion(lastQuestion);
        }

        // exception for network error
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // handles request errors
    @Override
    public void onErrorResponse(VolleyError error) {
        callback.gotError(error.getMessage());
        error.printStackTrace();
    }
}