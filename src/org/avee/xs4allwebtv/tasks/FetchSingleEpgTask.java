package org.avee.xs4allwebtv.tasks;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.URLS;
import org.avee.xs4allwebtv.util.EPGID;
import org.avee.xs4allwebtv.util.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class FetchSingleEpgTask extends BaseTask<Void, Void, JSONObject> {
	private ChannelAdapter adapter;
	private int position;
	private ChannelInfo channel;

	protected FetchSingleEpgTask(Activity context, ChannelAdapter adapter,
			int position) {
		super(context, null);
		this.adapter = adapter;
		this.position = position;
		channel = (ChannelInfo) adapter.getItem(position);
	}
	
	protected void onSuccess(JSONObject result) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		
	}
	
	@Override
	protected JSONObject doInBackground(Void... params) {
		try {
			JSONObject epg = HttpUtil.fetchJSONObject(URLS.EPG + "?current&next&channel="+channel.getEpgID());
			return epg;
		} catch (JSONException e) {
			error = e;
			return null;
		}
	}
}
