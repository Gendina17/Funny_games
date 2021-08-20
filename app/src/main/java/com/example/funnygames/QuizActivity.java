package com.example.funnygames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuizActivity extends AppCompatActivity {
    private Button but_next, but_ans;
    private EditText answer;
    private TextView question;
    private String string_answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        but_ans = findViewById(R.id.but_answ);
        but_next = findViewById(R.id.but_next);
        answer = findViewById(R.id.answer);
        question = findViewById(R.id.question);
        to_url();

        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_url();
            }
        });
    }

    private void to_url(){
        String url = "https://jservice.io/api/random?count=1";
        new GetUrlData().execute(url);
    }

    private class GetUrlData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                question.setText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}