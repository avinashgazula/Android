package com.example.avinash.testcamera;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //for sharedPreferences
    EditText passwordInput;
    TextView passwordText;

    //for notification
    NotificationCompat.Builder notification;
    public static final int uniqueNotificationID = 12345;


    //for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for sharedPreferences
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordText = (TextView) findViewById(R.id.passwordText);

        //for notification
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void savePassword(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", passwordInput.getText().toString());
        editor.apply();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void displayPassword(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String password = sharedPreferences.getString("password", "");
        passwordText.setText(password);
    }


    public void generateDummyNotification(View view){

        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("dummy Ticker");
        notification.setContentTitle("dummy Title");
        notification.setContentText("dummy text");
        notification.setWhen(System.currentTimeMillis());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds and issues notifications
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueNotificationID, notification.build());


    }

    public void takePhoto(View view){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //passing the taken picture to onActivityForResult. startActivityForResult because we want image back
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);
        }

    }
}
