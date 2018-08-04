//add this to android manifest
<uses-feature android:name="android.hardware.camera" android:required="true"></uses-feature>



//To check for a camera
//returns false if device has no camera else true
getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);


static final int REQUEST_IMAGE_CAPTURE = 1;

//this will open the camera and take the picture
Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//passing the taken picture to onActivityForResult. startActivityForResult because we want image back
startActivityForResult(i, REQUEST_IMAGE_CAPTURE);

//to get the picture back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data"); //data is the name of the intent
            imageView.setImageBitmap(photo);
        }

    }