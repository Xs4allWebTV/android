package org.avee.xs4allwebtv;

public final class URLS {
	/***
	 * Url to check if the current connection has access to WebTV. Return 1 when access is granted, 0 when denied.
	 */
	public static final String AUTHENTICATE = "http://webtv-api.xs4all.nl/1/authenticate.json";
	
	/***
	 * Url whith the list of channel available to the current user.
	 */
	public static final String LIST_CHANNELS = "http://webtv-api.xs4all.nl/1/listchannels.json";
	
	/*** 
	 * The JSON to get the stream URL. Takes 3 parameters, channel-key, format, quality.
	 * Known formats are 'apple' and 'silverlight-drm'.
	 * Known qualities are 'low', 'medium' and 'high'. Quality doesn't seem to matter with Silverlight 
	 * (it auto-adjusts). Quality 'hd' is also seen, but doesn't seem to work on anything.
	 */
	public static final String CHANNEL_STREAM = "http://webtv-api.xs4all.nl/1/channel/%1$s/%2$s/%3$s.json";
	
	/***
	 * URL of the logo image used in WebTV. Note it returns a logo which only works on a dark background
	 */
	public static final String CHANNEL_LOGO = "http://webtv.xs4all.nl/images/channels/%s.png";
	
	/***
	 * URL for fetching EPG information.
	 */
	public static final String EPG = "http://epg-api.xs4all.nl/";
}
