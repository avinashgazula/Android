package com.example.avinash.apple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import com.example.avinash.apple.MyService.LocalBinder;

public class MainActivity extends AppCompatActivity {

    MyService timeService;
    boolean isBound = false;
    TextView timeText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, MyService.class);
        bindService(i, timeConnection, Context.BIND_AUTO_CREATE);
    }

    public void getTime(View view){
        String currentTime = timeService.getCurrentTime();
        timeText = (TextView) findViewById(R.id.timeText);
        timeText.setText(currentTime);
    }

    //This class is responsible for binding app to the service
    private ServiceConnection timeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            timeService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
