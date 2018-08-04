package com.example.avinash.flappybird;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class startScreen extends AppCompatActivity {

    private TextView highScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        highScoreText = (TextView) findViewById(R.id.highScoreText);

        SharedPreferences sharedPreferences = getSharedPreferences("flappyBird", Context.MODE_PRIVATE);

        String highScoreString = sharedPreferences.getString("highScore", "0");
        highScoreText.setText("High Score : "+highScoreString);
    }

    public void startGame(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
