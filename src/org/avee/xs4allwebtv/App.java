package org.avee.xs4allwebtv;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;

public class App extends Application{

    private static Context mContext;

    private static ArrayList<ChannelInfo> channels;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
    
    public static String str(int key) {
    	return getContext().getString(key);
    }

	public static String strf(int key, Object...params) {
		return String.format(str(key), params);
	}
	
	public static void error(int key){
        AlertDialog.Builder altDialog = new AlertDialog.Builder(getContext());
        altDialog.setTitle(str(R.string.error));
        altDialog.setMessage(str(key));
        altDialog.setPositiveButton("OK", null);
        altDialog.show();
	}

	public static ArrayList<ChannelInfo> getChannels() {
		return channels;
	}

	public static void setChannels(ArrayList<ChannelInfo> channels) {
		App.channels = channels;
	}
}