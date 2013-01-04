package org.avee.xs4allwebtv.tasks;

import java.net.URL;
import java.net.URLConnection;

import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.URLS;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FetchChannelLogoTask extends BaseTask<Void, Void, Bitmap> {

	private ChannelAdapter adapter;
	private int position;

	protected FetchChannelLogoTask(Activity context, ChannelAdapter adapter, int position) {
		super(context, null);
		this.adapter = adapter;
		this.position = position;
	}

	@Override
	protected void onSuccess(Bitmap result) {
		ChannelInfo info = (ChannelInfo) adapter.getItem(position);
		info.setLogoImage(result);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		try {
			ChannelInfo info = (ChannelInfo) adapter.getItem(position);
			
	        // create a url object
	        URL url = new URL(String.format(URLS.CHANNEL_LOGO, info.getChannelKey()));

	        // create a urlconnection object
	        URLConnection urlConnection = url.openConnection();
	        return BitmapFactory.decodeStream(urlConnection.getInputStream());
		}
		catch(Exception e) {
			error = e;
			return null;
		}
	}

}
