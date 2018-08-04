//to save data
SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//userInfo is file name

SharedPreferences.Editor editor = sharedPreferences.edit();
editor.putString("string key word", /*string to be saved*/);
editor.apply();
Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();



//to retreive data
SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

String password = sharedPreferences.getString("string key word", "");//the second parameter is string to be returned if key not found
passwordText.setText(password);