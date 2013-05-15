package org.avee.xs4allwebtv;

import java.io.File;
import java.util.ArrayList;

import org.avee.xs4allwebtv.tasks.FetchChannelsTask;
import org.avee.xs4allwebtv.tasks.OpenStreamTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.setProperty("http.maxConnections", "8");

		try {
			File httpCacheDir = new File(getCacheDir(), "http");
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			// Create using reflection, android 4+ only.
			Class.forName("android.net.http.HttpResponseCache")
					.getMethod("install", File.class, long.class)
					.invoke(null, httpCacheDir, httpCacheSize);
		}
		catch (Exception httpResponseCacheNotAvailable) {
		}

		setContentView(R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.listChannels);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChannelInfo channel = (ChannelInfo) parent.getAdapter()
						.getItem(position);
				new OpenStreamTask(MainActivity.this).execute(channel);
			}
		});

		if (savedInstanceState != null) {
			try {
				ArrayList<ChannelInfo> channels = savedInstanceState
						.getParcelableArrayList("channels");
				if (channels != null) {
					ChannelAdapter adapter = new ChannelAdapter(this, channels);
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
		new FetchChannelsTask(this).execute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
			ListView listView = (ListView) findViewById(R.id.listChannels);
			ChannelAdapter apdapter = (ChannelAdapter) listView.getAdapter();
			ArrayList<ChannelInfo> channels = apdapter.getChannels();
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
}
