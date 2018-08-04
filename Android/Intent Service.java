//Service doesn't have an interface

//Intent Service


	//Create a new java class (example: TestIntentservice)
	import android.content.Intent;
	import android.support.annotation.Nullable;
	import android.app.IntentService;

	public class TestIntentservice extends IntentService {
		public TestIntentservice() {
			super("project name");
		}

		@Override
		protected void onHandleIntent(@Nullable Intent intent) {
			//This intent service runs whenever secondActivity is opened
			//code - example below(not working)
			//Toast.makeText(getApplicationContext(), "Intent Service Started", Toast.LENGTH_SHORT).show();
		}
	}


	//use this in the java class where the service is to be started

	Intent i = new Intent(this, TestIntentservice.class);
	startService(i);

	//Add this to AndroidManifest.xml right before </application>

	<service android:name=".TestIntentservice"/>