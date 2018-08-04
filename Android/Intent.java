//import statements

import android.view.View;
import android.content.Intent;


//using intent
Intent i = new Intent(this, /*class_name*/.class);
startActivity(i);

//sending extra intent data
	//-in the sending class
	i.putExtra("abcName", /*String to be sent*/);
	
	//-in the receiving class
	Bundle mainData = getIntent().getExtras();
    if(mainData == null)
		return;
	else{
		String text = mainData.getString("abcName");
	}
	
	