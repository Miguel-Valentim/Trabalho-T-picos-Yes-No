package com.tcc.trabandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText questionEditText;
    private Button decideButton;
    private TextView answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionEditText = findViewById(R.id.edt_pergunta);
        decideButton = findViewById(R.id.btn_resposta);
        answerTextView = findViewById(R.id.txt_resposta);

        decideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString();
                if (!question.isEmpty()) {
                    new FetchAnswerTask().execute(question);
                } else {
                    answerTextView.setText("Por favor, faça uma pergunta.");
                }
            }
        });
    }

    private class FetchAnswerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String question = params[0];
            String apiUrl = "https://yesno.wtf/api?question=" + question;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                reader.close();
                connection.disconnect();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                String answer = jsonResponse.getString("answer");
                answerTextView.setText(answer);
            } catch (JSONException e) {
                e.printStackTrace();
                answerTextView.setText("Erro ao obter a resposta.");
            }
        }
    }
}
