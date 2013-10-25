package org.avee.xs4allwebtv.client;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.text.format.DateUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebtvClient {
	private WebtvClient() {}
	/***
	 * Url to check if the current connection has access to WebTV. Return 1 when access is granted, 0 when denied.
	 */
	public static final String AUTHENTICATE = "https://webtv-api.xs4all.nl/2/authenticate.json";
	
	/***
	 * Url whith the list of channel available to the current user.
	 */
	public static final String LIST_CHANNELS = "https://webtv-api.xs4all.nl/2/listchannels.json";
	
	/*** 
	 * The JSON to get the stream URL. Takes 3 parameters, channel-key, format, quality.
	 * Known formats are 'apple' and 'silverlight-drm'.
	 * Known qualities are 'low', 'medium' and 'high'. Quality doesn't seem to matter with Silverlight 
	 * (it auto-adjusts). Quality 'hd' is also seen, but doesn't seem to work on anything.
	 */
	public static final String CHANNEL_STREAM = "https://webtv-api.xs4all.nl/2/channel/%1$s/%2$s/%3$s.json";
	
	/***
	 * URL of the logo image used in WebTV. Note it returns a logo which only works on a dark background
	 */
	public static final String CHANNEL_LOGO = "https://webtv.xs4all.nl/images/channels/%s.png";
	
	/***
	 * URL for fetching EPG information.
	 */
	public static final String EPG = "https://epg-api.xs4all.nl/";

	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void listChannels(AsyncHttpResponseHandler responseHandler) {
		client.get(LIST_CHANNELS, responseHandler);
	}
	
	public static void channelLogo(String channelKey, AsyncHttpResponseHandler responseHandler) {
		client.get(String.format(CHANNEL_LOGO, channelKey), responseHandler);
	}
	
	public static void channelEpg(String channelKey, AsyncHttpResponseHandler responseHandler) {
		long start = System.currentTimeMillis() - (2 * 60 * 60 * 1000);
		long end = System.currentTimeMillis() + (3 * 60 * 60 * 1000);
		client.get(EPG, 
				new RequestParams("channel", channelKey, 
								  "from", toISO(start),
								  "till", toISO(end)),
				responseHandler);
	}

	public static void channelStream(String channelKey, String format, String quality, AsyncHttpResponseHandler responseHandler) {
		client.get(String.format(CHANNEL_STREAM, channelKey, format, quality), responseHandler);
	}
	
	private static String toISO(long millis) {
		return DateFormatUtils.ISO_DATETIME_FORMAT.format(millis);
	}
}
