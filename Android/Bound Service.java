//Example- creating a new service MyService

package /*package name*/;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
//for time
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


//in the main application 

MyService testService;
boolean isBound = false;


//This class is responsible for binding app to the service
private ServiceConnection timeConnection = new ServiceConnection() {
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		LocalBinder binder = (LocalBinder) service;
		testService = binder.getService();
		isBound = true;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		isBound = false;
	}
};

//to start the service

Intent i = new Intent(this, MyService.class);
bindService(i, timeConnection, Context.BIND_AUTO_CREATE);


//to access the function getCurrentTime in MyService
String currentTime = testService.getCurrentTime();

