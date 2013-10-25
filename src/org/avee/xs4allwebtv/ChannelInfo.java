package org.avee.xs4allwebtv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.avee.xs4allwebtv.client.WebtvClient;
import org.avee.xs4allwebtv.util.EpgMode;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.WebImage;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

public class ChannelInfo implements Parcelable {

	private String channelKey;
	private String channelName;
	
	private String currentProgram;
	private String nextProgram;
	private Date currentStart;
	private Date nextStart;
	
	private Date epgAge;
	private SmartImage logoImage;

	public ChannelInfo(String key, String name) {
		this.channelKey = key;
		this.channelName = name;
		this.currentProgram = App.str(R.string.loading_epg);
		this.nextProgram = "";
		this.currentStart = new Date();
		this.nextStart = new Date();
		this.logoImage = new WebImage(String.format(WebtvClient.CHANNEL_LOGO, channelKey));
	}
	
	public ChannelInfo(Parcel in) {
		this.channelKey = in.readString();
		this.channelName = "* " + in.readString();
		this.currentProgram = in.readString();
		this.nextProgram = in.readString();
		this.currentStart = (Date) in.readValue(null);
		this.nextStart = (Date) in.readValue(null);
		this.logoImage = new WebImage(String.format(WebtvClient.CHANNEL_LOGO, channelKey));
	}

	public String getChannelKey() {
		return channelKey;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getCurrentProgram() {
		return currentProgram;
	}
	
	public void setEpgData(JSONArray epg, EpgMode mode)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		Date now = new Date();

		if(mode == EpgMode.All)
		{
			try	{
				for(int j = 0; j < epg.length(); j++) {
					JSONObject current = epg.getJSONObject(j);
					Date start = df.parse(current.getString("time_start"));
					Date end = df.parse(current.getString("time_end"));
					if(start.before(now) && end.after(now)) {
						currentProgram = Html.fromHtml(current.getString("title")).toString();
						currentStart = start;
						try {
							JSONObject next = epg.getJSONObject(j+1); // assumption: EPG in time order!
							nextProgram = Html.fromHtml(next.getString("title")).toString();
							nextStart = df.parse(next.getString("time_start"));
						} catch (Exception e) {
							// fetching the next program failed for some reason
							nextProgram = String.format(App.str(R.string.epg_error), e.getMessage());
							nextStart = end;
						}
						epgAge = now;
						break;
					} else {
						currentProgram = App.str(R.string.no_epg_data);
					}
				}
			} catch (Exception e) {
				// fetching the next program failed for some reason
				currentProgram = App.strf(R.string.epg_error, e.getMessage());
				currentStart = now;
			}
		}
		else 
		{
			try
			{
				JSONObject current = epg.getJSONObject(0);
				Date start = df.parse(current.getString("time_start"));
				
				if(mode == EpgMode.Now)
				{
					currentProgram = Html.fromHtml(current.getString("title")).toString();
					currentStart = start;
				}
				else
				{
					nextProgram = Html.fromHtml(current.getString("title")).toString();
					nextStart = df.parse(current.getString("time_start"));
				}
			} catch (Exception e) {
				// fetching the next program failed for some reason
				currentProgram = String.format(App.str(R.string.epg_error), e.getMessage());
				currentStart = now;
			}
		}
	}
	
	public String getNextProgram() {
		return nextProgram;
	}

	public Date getCurrentStart() {
		return currentStart;
	}

	public Date getNextStart() {
		return nextStart;
	}

	public Date getEpgAge() {
		return epgAge;
	}
	
	public SmartImage getLogoImage() {
		return logoImage;
	}
	
	@Override
	public String toString() {
		return channelName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(channelKey);
		dest.writeString(channelName);
		dest.writeString(currentProgram);
		dest.writeString(nextProgram);
		dest.writeValue(currentStart);
		dest.writeValue(nextStart);
	}

	public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {
		public ChannelInfo createFromParcel(Parcel in) {
			return new ChannelInfo(in);
		}

		public ChannelInfo[] newArray(int size) {
			return new ChannelInfo[size];
		}
	};
}
