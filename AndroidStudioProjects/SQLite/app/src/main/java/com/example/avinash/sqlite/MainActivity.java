package com.example.avinash.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity {

    TextView databaseText;
    EditText bandInput;
    MyDBHandler dbHandler;
    VideoView testVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testVideo = (VideoView) findViewById(R.id.testVideo);
        bandInput = (EditText) findViewById(R.id.bandInput);
        databaseText = (TextView) findViewById(R.id.databaseText);
        dbHandler = new MyDBHandler(this , "", null, 1);
        printDatabase();
    }

    public void startVideo(View view){
        testVideo.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4");

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(testVideo);
        testVideo.setMediaController(mediaController);

        testVideo.start();
    }

    public void addToDatabase(View view){
        Bands band = new Bands(bandInput.getText().toString());
        dbHandler.addRow(band);
        printDatabase();
    }

    public void deleteFromDatabase(View view){
        String bandName = bandInput.getText().toString();
        dbHandler.deleteRow(bandName);
        printDatabase();
    }

    public void printDatabase(){
        String database = dbHandler.convertDatabaseToString();
        databaseText.setText(database);
        bandInput.setText("");
    }

}
