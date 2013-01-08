package org.avee.xs4allwebtv.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.URLS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.EPGID;
import util.HttpUtil;

import android.app.Activity;
import android.text.Html;

public class FetchEpgTask extends BaseTask<Void, Void, JSONObject> {
	private ChannelAdapter adapter;

	protected FetchEpgTask(Activity context, ChannelAdapter adapter) {
		super(context, null);
		this.adapter = adapter;
	}

	@Override
	protected void onSuccess(JSONObject result) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		for(int i = 0; i < adapter.getCount(); i++) {
			Date now = new Date(); 
			ChannelInfo channel = (ChannelInfo) adapter.getItem(i);
			try {
				String epgID = EPGID.get(channel.getChannelKey());
				JSONArray epg = result.getJSONArray(epgID);
				for(int j = 0; j < epg.length(); j++) {
					JSONObject program = epg.getJSONObject(j);
					Date start = df.parse(program.getString("time_start"));
					Date end = df.parse(program.getString("time_end"));
					if(start.before(now) && end.after(now)) {
						String title = Html.fromHtml(program.getString("title")).toString();
						channel.setCurrentProgram(title);
						break;
					}
				}
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
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
