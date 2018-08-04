package com.example.avinash.flappybird;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private int score = 0;
    private int highScore = 0;

    TextView scoreText,highScoreText;

    private ImageView pipeTopImage,pipeBottomImage;
    private ImageView birdImage;

    private int frameHeight,frameWidth;
    RelativeLayout gameLayout;


    private float pipeTopX,pipeTopY;
    private float pipeBottomX,pipeBottomY;
    private float birdX,birdY;
    float poleDistance;

    private int pipeSpeed;

    public boolean touch_flag = false;
    public boolean start_flag = false;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreText = (TextView) findViewById(R.id.scoreText);
        highScoreText = (TextView) findViewById(R.id.highScoreText);
        pipeTopImage = (ImageView) findViewById(R.id.pipeTopImage);
        pipeBottomImage = (ImageView) findViewById(R.id.pipeBottomImage);
        birdImage = (ImageView) findViewById(R.id.birdImage) ;
        gameLayout = (RelativeLayout) findViewById(R.id.gameLayout);


        SharedPreferences sharedPreferences = getSharedPreferences("flappyBird", Context.MODE_PRIVATE);

        String highScoreString = sharedPreferences.getString("highScore", "0");
        highScore = Integer.parseInt(highScoreString);
        highScoreText.setText("High Score : "+highScore);

        birdX = 40;
        birdY = 500;
        birdImage.setX(birdX);
        birdImage.setY(birdY);

        pipeTopY = -800f;
        pipeTopX = 600f;
        pipeTopImage.setY(pipeTopY);
        pipeTopImage.setX(pipeTopX);


        pipeBottomY = 1500f;
        pipeBottomX = 600f;
        pipeBottomImage.setY(pipeBottomY);
        pipeBottomImage.setX(pipeBottomX);

        Toast.makeText(this, "Tap to Start", Toast.LENGTH_SHORT).show();

    }

    public void changePosition(){



        frameHeight= gameLayout.getHeight();
        frameWidth = gameLayout.getWidth();
       // scoreText.setText(""+frameHeight);

        //check if out
        result();

        poleDistance = (frameHeight/5);

        //Top Pipe

        if(pipeTopX + pipeTopImage.getWidth() < 0){
            pipeTopX = frameWidth;
            pipeBottomX = frameWidth;

            pipeBottomY =(float)Math.random() * ((2*frameHeight)/3);
            pipeBottomY += (frameHeight/3) - 150;

            pipeTopY = pipeBottomY - (poleDistance + pipeTopImage.getHeight());

            //Toast.makeText(this, ""+pipeBottomY, Toast.LENGTH_SHORT).show();


        }
        else{
            pipeTopX -= 10; //pipeSpeed
            pipeBottomX -=10;
        }

        pipeTopImage.setX(pipeTopX);
        pipeTopImage.setY(pipeTopY);
        pipeBottomImage.setX(pipeBottomX);
        pipeBottomImage.setY(pipeBottomY);

        //bird
        if(touch_flag){
            birdY -= 20;
        }
        else{
            birdY += 10;
        }
        if(birdY >= 0 && birdY <= frameHeight - birdImage.getHeight() )
            birdImage.setY(birdY);

    }

    public void result(){

        float birdCenterX = birdImage.getX() + (birdImage.getWidth()/2);
        float birdCenterY = birdImage.getY() + (birdImage.getHeight()/2);

        if(birdCenterX >= pipeBottomX && birdCenterX <= pipeBottomX + pipeBottomImage.getWidth()
                && (birdCenterY <= pipeTopY + pipeTopImage.getHeight() || birdCenterY >= pipeBottomY)){
            scoreText.setText("Score : "+score);
            timer.cancel();

            if(score > highScore){

                highScore = score;
                highScoreText.setText("High Score : "+highScore);

                SharedPreferences sharedPreferences = getSharedPreferences("flappyBird", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("highScore",""+score);
                editor.commit();
            }
            SharedPreferences sharedPreferences = getSharedPreferences("flappyBird", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("score", ""+score);
            editor.commit();

            Intent i = new Intent(this, endScreen.class);
            startActivity(i);

        }
        else if (pipeTopX + pipeTopImage.getWidth() < 0){
            score ++;
            scoreText.setText("Score : "+score);
            if(score > highScore)
                highScoreText.setText("High Score : "+ score);
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(start_flag == false){
            start_flag = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePosition();
                        }
                    });
                }
            }, 0, 20);
        }else {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                touch_flag = true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                touch_flag = false;
            }
        }

        return true;
    }
}
