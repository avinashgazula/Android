package com.example.avinash.flappybird;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class endScreen extends AppCompatActivity {

    private TextView scoreText;
    private TextView highScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        scoreText = (TextView) findViewById(R.id.scoreText);
        highScoreText = (TextView) findViewById(R.id.highScoreText);

        SharedPreferences sharedPreferences = getSharedPreferences("flappyBird", Context.MODE_PRIVATE);

        String highScoreString = sharedPreferences.getString("highScore", "0");
        String scoreString = sharedPreferences.getString("score", "0");
        highScoreText.setText("High Score : "+highScoreString);
        scoreText.setText("Current Score : "+ scoreString);
    }

    public void replayGame(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
