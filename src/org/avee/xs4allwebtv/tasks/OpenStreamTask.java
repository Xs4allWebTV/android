package org.avee.xs4allwebtv.tasks;

import org.avee.xs4allwebtv.ChannelInfo;
import org.avee.xs4allwebtv.MainActivity;
import org.avee.xs4allwebtv.URLS;
import org.avee.xs4allwebtv.VideoActivity;
import org.json.JSONObject;

import util.HttpUtil;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

public class OpenStreamTask extends BaseTask<ChannelInfo, Void, String> {
	private MainActivity context;

	public OpenStreamTask(MainActivity context) {
		super(context, "Fetching Stream URL");
		this.context = context;
	}
    
    @Override
	protected String doInBackground(ChannelInfo... args) {
		try	{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			String quality = pref.getString("videoQuality", "high");
	    	JSONObject obj = HttpUtil.fetchJSONObject(String.format(URLS.CHANNEL_STREAM, args[0].getChannelKey(), "apple", quality));
	    	return obj.getString("streamurl");
		}
		catch(Exception e) {
			error = e;
			return null;
		}
	}

    @Override
    protected void onSuccess(String result) {
        try {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			Boolean playExternal = pref.getBoolean("videoExternal", false);
			Intent i = new Intent();
	    	if(!playExternal)
	    		i.setClass(context, VideoActivity.class);
	        i.setAction(Intent.ACTION_VIEW);
	        Uri uri = Uri.parse(result);
	        i.setDataAndType(uri, "video/*");
	        context.startActivity(i);
        } catch(Exception e) {
        	AlertDialog.Builder altDialog = new AlertDialog.Builder(context);
        	altDialog.setTitle("Error");
			altDialog.setMessage(e.getMessage());
			altDialog.setPositiveButton("OK", null);
			altDialog.show();
        }
    }
}
