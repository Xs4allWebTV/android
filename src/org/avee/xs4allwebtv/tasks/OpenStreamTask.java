package org.avee.xs4allwebtv.tasks;

import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.MainActivity;
import org.avee.xs4allwebtv.URLS;
import org.json.JSONObject;

import util.HttpUtil;
import android.content.Intent;
import android.net.Uri;

public class OpenStreamTask extends BaseTask<ChannelInfo, Void, String> {
	private MainActivity context;

	public OpenStreamTask(MainActivity context) {
		super(context, "Fetching Stream URL");
		this.context = context;
	}
    
    @Override
	protected String doInBackground(ChannelInfo... args) {
		try	{
	    	JSONObject obj = HttpUtil.fetchJSONObject(String.format(URLS.CHANNEL_STREAM, args[0].getChannelKey(), "apple", "high"));
	    	return obj.getString("streamurl");
		}
		catch(Exception e) {
			error = e;
			return null;
		}
	}

    @Override
    protected void onSuccess(String result) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(result);
        i.setDataAndType(uri, "video/*");
        context.startActivity(i);
    }
}
