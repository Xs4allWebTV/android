package org.avee.xs4allwebtv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean forceLandscape = pref.getBoolean("videoLandscape", true);
		if(forceLandscape)
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.video_view);
		
		Intent intent = getIntent();
		Uri uri = intent.getData();
		VideoView vv = (VideoView) findViewById(R.id.videoview);
		vv.setVideoURI(uri);
		vv.requestFocus();
		vv.start();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus) {
			VideoView vv = (VideoView) findViewById(R.id.videoview);
			if(!vv.isPlaying())
				vv.start();
		}
	}
}
