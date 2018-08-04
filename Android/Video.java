import android.widget.VideoView;
//for media controls
import android.widget.MediaController;

VideoView testVideo = (VideoView) findViewById(R.id.testVideo);

testVideo.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4"); //video path

//Media Controls for the video
MediaController mediaController = new MediaController(this);
mediaController.setAnchorView(testVideo);
testVideo.setMediaController(mediaController);
//starting the video
testVideo.start();

//add permission for internet in androidManifest