package org.avee.xs4allwebtv;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class VideoViewWrapper {
	private android.widget.VideoView videoView;
	private io.vov.vitamio.widget.VideoView vitamioView;
	
	public VideoViewWrapper(Context context) {
		createView(context);
	}
	
	private void createView(Context context)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useVitamio = pref.getBoolean("useVitamio", false);
		View view;
		if(useVitamio) {
			vitamioView = new io.vov.vitamio.widget.VideoView(context);
			view = vitamioView;
		} else {
			videoView = new android.widget.VideoView(context);
			view = videoView;
		}
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void play(Uri uri)
	{
		if(videoView != null) {
			videoView.setVideoURI(uri);
			videoView.requestFocus();
			videoView.start();
		} else if(vitamioView != null) {
			vitamioView.setVideoURI(uri);
			vitamioView.requestFocus();
			vitamioView.start();
		}
	}

	public View getView() {
		return videoView != null ? videoView : vitamioView;
	}

	public boolean isPlaying() {
		if(videoView != null) {
			return videoView.isPlaying();
		} else if(vitamioView != null) {
			return vitamioView.isPlaying();
		} else {
			return false;
		}
	}

	public void start() {
		if(videoView != null) {
			videoView.start();
		} else if(vitamioView != null) {
			vitamioView.start();
		}
	}	
}
