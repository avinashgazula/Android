//Gestures

//import statements
import android.support.v4.view.GestureDetectorCompat;
import android.view.View.OnScrollChangeListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.GestureDetector;


//code to enable touch gestures

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
	
	
	
//start of gestures'



* @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
		
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
		
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
		
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
		
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
		
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
		
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
		
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		
        return true;
    }
	
	
//end of gestures