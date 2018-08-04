package com.example.avinash.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements topFragmentClass.topActivityListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void createMeme(String top, String bottom) {

        bottomFragmentClass bottomMeme = (bottomFragmentClass) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        bottomMeme.setMemeText(top, bottom);

    }
}
