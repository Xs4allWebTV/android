package org.avee.xs4allwebtv.util;

public final class EPGID {
	/***
	 * For some channels it's ID in EPG data is different from the channel ID. This 
	 * maps between those ID's. The list is hardcoded for now, its values are taken from 
	 * http://webtv.xs4all.nl/js/channels.js We could consider parsing that file...
	 * 
	 * @param channelID The channel ID
	 * @return The ID of the channel for EPG data.
	 */
	public static String get(String channelID) {
		if("canvas".equals(channelID))
			return "kc";
		else if("discovery".equals(channelID))
			return "disc";
		else if("humortv24".equals(channelID))
			return "humor24";
		else if("nos24".equals(channelID))
			return "journaal24";
		else
			return channelID;
	}
}
