package com.example.avinash.apple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    private final IBinder timeBinder = new LocalBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return timeBinder;
    }

    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        return df.format(new Date());
    }

    //MyService doesn't have the ability to bind as it doesn't inherit from "Binder" class so we create a new class which can bind
    public class LocalBinder extends Binder{
        MyService getService(){
            //returns a reference to the parent class
            return MyService.this;
        }
    }

}
