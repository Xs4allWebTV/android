package org.avee.xs4allwebtv.tasks;

import java.util.ArrayList;
import org.avee.xs4allwebtv.ChannelAdapter;
import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.R;
import org.avee.xs4allwebtv.URLS;
import org.json.JSONArray;
import org.json.JSONObject;

import util.HttpUtil;
import android.app.Activity;
import android.widget.ListView;

public class FetchChannelsTask extends BaseTask<Void, Void, ArrayList<ChannelInfo>> {

	/**
	 * @param mainActivity
	 */
	public FetchChannelsTask(Activity context) {
		super(context, "Fetching Channels");
	}

	@Override
	protected ArrayList<ChannelInfo> doInBackground(Void... arg0) {
		try {
			String auth = HttpUtil.getUrlContents(URLS.AUTHENTICATE);
			
			if(auth == null || !auth.trim().equals("1"))
				throw new Exception("No Access");
    		
			JSONObject obj = HttpUtil.fetchJSONObject(URLS.LIST_CHANNELS);
    		JSONArray jsonArray = obj.getJSONArray("channels");
    		//ChannelInfo[] result = new ChannelInfo[jsonArray.length()];
    		ArrayList<ChannelInfo> result = new ArrayList<ChannelInfo>();
	    	for(int i = 0; i < jsonArray.length(); i++) {
		    		JSONObject jsonObject = jsonArray.getJSONObject(i);
		    	if(!jsonObject.has("local") || !jsonObject.getBoolean("local")) {
		    		String id = jsonObject.getString("id");
		    		String name = jsonObject.getString("name");
		    		result.add( new ChannelInfo(id, name ) );
	    		}
	    	}
	    	return result;
		} 
		catch(Exception ex) {
			error = ex;
			return null;
		}
	}
    
	@Override
	protected void onSuccess(ArrayList<ChannelInfo> result) {
		ChannelAdapter adapt = new ChannelAdapter(context, result);
		ListView listView = (ListView) context.findViewById(R.id.listChannels);
		listView.setAdapter(adapt);
		for(int i = 0; i < result.size(); i++)
			new FetchChannelLogoTask(context, adapt, i).execute();
		new FetchEpgTask(context, adapt).execute();
	}
}