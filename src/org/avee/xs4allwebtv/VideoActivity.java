package org.avee.xs4allwebtv;

import java.util.ArrayList;

import org.avee.xs4allwebtv.client.WebtvClient;
import org.json.JSONException;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;

public class VideoActivity extends Activity {
	VideoViewWrapper videoWrapper;
	protected static ProgressDialog busyIndicator;
	private String currentChannel;
	
	public static void start(String channel) {
        try {
    		Intent i = new Intent();
    		i.setClass(App.getContext(), VideoActivity.class);
	        i.setAction(Intent.ACTION_VIEW);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.putExtra("channel", channel);
	        App.getContext().startActivity(i);
        } catch(Exception e) {
        	AlertDialog.Builder altDialog = new AlertDialog.Builder(App.getContext());
        	altDialog.setTitle("Error");
			altDialog.setMessage(e.getMessage());
			altDialog.setPositiveButton("OK", null);
			altDialog.show();
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean forceLandscape = pref.getBoolean("videoLandscape", true);
		if(forceLandscape)
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.video_view);

		FrameLayout layout = (FrameLayout) findViewById(R.id.video_layout);
		videoWrapper = new VideoViewWrapper(this);
		View view = videoWrapper.getView();
		layout.addView(view, 0);
		
		Intent intent = getIntent();
		play(intent.getStringExtra("channel"));
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && videoWrapper != null &&! videoWrapper.isPlaying()) {
			videoWrapper.start();
		}
	}
	
	public void nextChannel(View v) {
		ArrayList<ChannelInfo> channels = App.getChannels();
		for(int i = 0; i < channels.size(); i++) {
			if(channels.get(i).getChannelKey().equals(currentChannel)) {
				play(channels.get(++i % channels.size()).getChannelKey());
				return;
			}
		}
	}
	
	public void prevChannel(View v) {
		ArrayList<ChannelInfo> channels = App.getChannels();
		for(int i = 0; i < channels.size(); i++) {
			if(channels.get(i).getChannelKey().equals(currentChannel)) {
				play(channels.get((--i + channels.size()) % channels.size()).getChannelKey());
				return;
			}
		}
	}
	
	public void play(String channel) {
		busy("Fetching Stream URL");
		currentChannel = channel;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
		String quality = pref.getString("videoQuality", "high");
		WebtvClient.channelStream(channel, "apple", quality, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(org.json.JSONObject arg0) {
				try {
					busyIndicator.dismiss();
					videoWrapper.play(Uri.parse(arg0.getString("streamurl")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

	private void busy(String message) {
		if(busyIndicator == null) {
			busyIndicator = new ProgressDialog(this);
			busyIndicator.setIndeterminate(true);
			busyIndicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		busyIndicator.setMessage(message);
		busyIndicator.show();
	}
}
