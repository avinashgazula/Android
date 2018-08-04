//Don't change an interface element in a thread. Use handlers to change interface elements

Runnable r = new Runnable(){
	public void run(){
		//code
		handler.sendEmptyMessage(0); // same handler name used in line 21
	}
};

Thread t1= new Thread(r);
t1.start;

//Implementing Thread handlers

import android.os.Handler;
import android.os.Message;


//in Main Activity

Handler handler = new Handler(){
	public void handleMessage(Message msg){
		super.handleMessage(msg);
	}
}