package org.avee.xs4allwebtv.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.R;
import org.avee.xs4allwebtv.URLS;
import org.avee.xs4allwebtv.util.EPGID;
import org.avee.xs4allwebtv.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
					JSONObject currentProgram = epg.getJSONObject(j);
					Date currentStart = df.parse(currentProgram.getString("time_start"));
					Date currentEnd = df.parse(currentProgram.getString("time_end"));
					if(currentStart.before(now) && currentEnd.after(now)) {
						String currentTitle = Html.fromHtml(currentProgram.getString("title")).toString();
						channel.setCurrentProgram(currentTitle);
						channel.setCurrentStart(currentStart);
						try {
							JSONObject nextProgram = epg.getJSONObject(j+1); // assumption: EPG in time order!
							String nextTitle = Html.fromHtml(nextProgram.getString("title")).toString();
							Date nextStart = df.parse(nextProgram.getString("time_start"));
							channel.setNextProgram(nextTitle);
							channel.setNextStart(nextStart);
						} catch (JSONException e) {
							// fetching the next program failed for some reason
							channel.setNextProgram(context.getString(R.string.no_epg_data));
							channel.setNextStart(currentEnd);
						}
						
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
