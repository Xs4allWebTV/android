package org.avee.xs4allwebtv.client;

import java.util.ArrayList;

import org.avee.xs4allwebtv.ChannelInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class ChannelsResponseHandler extends JsonHttpResponseHandler {
	@Override
	public void onSuccess(JSONObject obj) {
		try {
			JSONArray jsonArray = obj.getJSONArray("channels");
    		ArrayList<ChannelInfo> result = new ArrayList<ChannelInfo>();
	    	for(int i = 0; i < jsonArray.length(); i++) {
		    		JSONObject jsonObject = jsonArray.getJSONObject(i);
		    	if(!jsonObject.has("local") || !jsonObject.getBoolean("local")) {
		    		String id = jsonObject.getString("id");
		    		String name = jsonObject.getString("name");
		    		result.add( new ChannelInfo(id, name ) );
	    		}
	    	}
	    	onSuccess(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public abstract void onSuccess(ArrayList<ChannelInfo> channels);
}
