package org.avee.xs4allwebtv.tasks;

import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.URLS;
import org.avee.xs4allwebtv.util.EPGID;
import org.avee.xs4allwebtv.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class FetchEpgTask extends BaseTask<Void, Void, JSONObject> {
	private ChannelAdapter adapter;
	
	protected FetchEpgTask(Activity context, ChannelAdapter adapter) {
		super(context, null);
		this.adapter = adapter;
	}

	@Override
	protected void onSuccess(JSONObject result) {
		
		for(int i = 0; i < adapter.getCount(); i++) {
			ChannelInfo channel = (ChannelInfo) adapter.getItem(i);
			try {
				String epgID = EPGID.get(channel.getChannelKey());
				JSONArray epg = result.getJSONArray(epgID);
				channel.setEpgData(epg);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		try {
			JSONObject epg = HttpUtil.fetchJSONObject(URLS.EPG);
			return epg;
		} catch (JSONException e) {
			error = e;
			return null;
		}
	}

}
