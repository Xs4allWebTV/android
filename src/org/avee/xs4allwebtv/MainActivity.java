package org.avee.xs4allwebtv;

import java.util.ArrayList;

import org.avee.xs4allwebtv.client.ChannelsResponseHandler;
import org.avee.xs4allwebtv.client.WebtvClient;
import org.avee.xs4allwebtv.util.EpgMode;
import org.json.JSONException;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	protected ProgressDialog busyIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		String path = getCacheDir().getAbsolutePath();
		System.err.println(path);
		
		final ListView listView = (ListView) findViewById(R.id.listChannels);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChannelInfo channel = (ChannelInfo) parent.getAdapter()
						.getItem(position);
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
				Boolean playExternal = pref.getBoolean("videoExternal", false);
		    	if(!playExternal) {
		    		VideoActivity.start(channel.getChannelKey());
		    	} else {
		    		busy("Fetching Stream URL");
		    		String quality = pref.getString("videoQuality", "high");
		    		WebtvClient.channelStream(channel.getChannelKey(), "apple", quality, new JsonHttpResponseHandler() {
		    			public void onSuccess(org.json.JSONObject arg0) {
		    				try {
					    		Intent i = new Intent();
						        i.setAction(Intent.ACTION_VIEW);
						        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						        Uri uri = Uri.parse(arg0.getString("streamurl"));
						        i.setDataAndType(uri, "video/*");
						        busyIndicator.dismiss();
						        App.getContext().startActivity(i);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			}
		    		});
		    	}
			}
		});

		if (savedInstanceState != null) {
			try {
				ArrayList<ChannelInfo> channels = savedInstanceState
						.getParcelableArrayList("channels");
				if (channels != null) {
					App.setChannels(channels);
					ChannelAdapter adapter = new ChannelAdapter();
					listView.setAdapter(adapter);

					// Restored state, so we're done.
					return;
				}
			} catch (Exception e) {
				// Never crash while saving.
				Log.e("org.avee.xs4allwebtv.MainActivity",
						"Error restoring state", e);
			}
		}

		// Didn't restore, fetch...
		busy("Fetching Channels");
		WebtvClient.listChannels(new ChannelsResponseHandler() {
			@Override
            public void onSuccess(ArrayList<ChannelInfo> channels) {
				App.setChannels(channels);
				final ChannelAdapter adapt = new ChannelAdapter();
				listView.setAdapter(adapt);
				busyIndicator.dismiss();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
			ListView listView = (ListView) findViewById(R.id.listChannels);
			ChannelAdapter adapter = (ChannelAdapter) listView.getAdapter();
			ArrayList<ChannelInfo> channels = adapter.getChannels();
			outState.putParcelableArrayList("channels", channels);
		} catch (Exception e) {
			// Never crash while saving.
			Log.e("org.avee.xs4allwebtv.MainActivity", "Error saving state", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus) {
			ListView listView = (ListView) findViewById(R.id.listChannels);
			final ChannelAdapter adapt = (ChannelAdapter) listView.getAdapter();
			ArrayList<ChannelInfo> channels = adapt.getChannels();
			for (final ChannelInfo channelInfo : channels) {
				if(channelInfo.getNextStart() == null || channelInfo.getEpgAge() == null || 
						channelInfo.getNextStart().getTime() <= System.currentTimeMillis() || 
						System.currentTimeMillis() - channelInfo.getEpgAge().getTime() > 10 * 60 * 1000) {
					WebtvClient.channelEpg(channelInfo.getChannelKey(), new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(org.json.JSONObject arg0) {
							try {
								channelInfo.setEpgData(arg0.getJSONArray(channelInfo.getChannelKey()), EpgMode.All);
								adapt.notifyDataSetChanged();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						};
					});
				}
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		try {
			Object cache = Class.forName("android.net.http.HttpResponseCache")
								.getMethod("getInstalled")
								.invoke(null);
			if(cache != null) {
				Class.forName("android.net.http.HttpResponseCache")
				.getMethod("flush")
				.invoke(cache);
			}
		} catch (Exception e) {
		}
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
