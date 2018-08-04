package com.example.avinash.overflowmenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.transition.TransitionManager;

public class MainActivity extends AppCompatActivity {

    ViewGroup mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (ViewGroup) findViewById(R.id.mainLayout);

        mainLayout.setOnTouchListener(
                new RelativeLayout.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){

                        TransitionManager.beginDelayedTransition(mainLayout);

                        View button = findViewById(R.id.button);
                        RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        position.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                        position.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);

                        button.setLayoutParams(position);


                        ViewGroup.LayoutParams size = button.getLayoutParams();
                        size.height=200;
                        size.width=400;

                        button.setLayoutParams(size);

                        return true;

                    }

                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.Red) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mainLayout.setBackgroundColor(Color.RED);
            return true;
        }
        else if (id == R.id.Blue) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mainLayout.setBackgroundColor(Color.BLUE);
            return true;
        }
        else if (id == R.id.Yellow) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mainLayout.setBackgroundColor(Color.YELLOW);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);

    }
}
