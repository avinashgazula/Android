//Button

//import android.widget.Button;

	helloButton = (Button)findViewById(R.id.hello);

	helloButton.setOnClickListener(
		new Button.OnClickListener(){
			public void onClick(View v){
                        

			}}
	);

	helloButton.setOnLongClickListener(
			new Button.OnLongClickListener(){
				public boolean onLongClick(View v){
					
					return true; //if returned false it will perform the actions of both "onClick' and "onLongClick"
				}
			}
	);
