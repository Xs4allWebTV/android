package org.avee.xs4allwebtv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class VideoControllerLayout extends FrameLayout implements OnGestureListener {
	private GestureDetector gestureDetector;
	private VideoActivity videoActivity;
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	
	public VideoControllerLayout(Context context) {
		super(context);
		initialize(context);
	}

	public VideoControllerLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	public VideoControllerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	private void initialize(Context context) {
		if(context instanceof VideoActivity)
			videoActivity = (VideoActivity) context;
		gestureDetector = new GestureDetector(context, this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(videoActivity == null)
			return false;
		try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                next();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                prev();
                return true;
            }
        } catch (Exception e) {
            // nothing
        }
        return false;	}

	private void prev() {
		videoActivity.prevChannel(null);
	}

	private void next() {
		videoActivity.nextChannel(null);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		View view = findViewById(R.id.channel_switcher);
		if(view.getVisibility() == VISIBLE)
			view.setVisibility(GONE);
		else
			view.setVisibility(VISIBLE);
		return true;
	}
}
