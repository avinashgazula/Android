    //for notification
    NotificationCompat.Builder notification;
    public static final int uniqueNotificationID = 12345;
	
	
//onCreate
//for notification
notification = new NotificationCompat.Builder(this);
notification.setAutoCancel(true); //removes notification from status bar after app is opened
		
//notification infromation		
notification.setSmallIcon(R.mipmap.ic_launcher);
notification.setTicker("dummy Ticker");
notification.setContentTitle("dummy Title");
notification.setContentText("dummy text");
notification.setWhen(System.currentTimeMillis()); //shows time in the notification

//to let the phone know that we are accesing notifications
Intent intent = new Intent(this, MainActivity.class);
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
notification.setContentIntent(pendingIntent);

//Builds and issues notifications
NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
notificationManager.notify(uniqueNotificationID, notification.build());