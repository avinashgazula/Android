import android.content.Intent;
import android.view.View;

//Sending Broadcasts

	Intent i = new Intent();
	i.setAction("/*package_name*/");
	i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); //so that the broadcast works in all versions of android
	sendBroadcast(i);
	
	
//Receiving Broadcasts

	//create a new broadcast receiver and write the action to be performed on receiving the broadcast in "onReceive" function

    public void onReceive(Context context, Intent intent) {
       // Toast.makeText(context,"Broadcast Received",Toast.LENGTH_LONG).show();
    }
	
	//And in AndroidManifest.xml and <receiver> add the intent filter
		<receiver
            android:name=".ReceiveBroadcast"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="/*package name of sent package*/">
                </action>
            </intent-filter>
        </receiver>