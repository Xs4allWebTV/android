package org.avee.xs4allwebtv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.avee.xs4allwebtv.util.EPGID;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

public class ChannelInfo implements Parcelable {

	private String channelKey;
	private String channelName;
	private String epgID;
	
	private String currentProgram;
	private String nextProgram;
	private Date currentStart;
	private Date nextStart;
	private Date nextEnd;
	
	private Date epgAge;

	private Bitmap logoImage;

	public ChannelInfo(String key, String name) {
		this.channelKey = key;
		this.channelName = name;
		this.currentProgram = "Geen gidsinformatie beschikbaar";
		this.nextProgram = "Geen gidsinformatie beschikbaar";
		this.currentStart = new Date();
		this.nextStart = new Date();
		this.epgID = EPGID.get(key);
	}
	
	public ChannelInfo(Parcel in) {
		this.channelKey = in.readString();
		this.channelName = "* " + in.readString();
		this.logoImage = in.readParcelable(null);
		this.currentProgram = in.readString();
		this.nextProgram = in.readString();
		this.currentStart = (Date) in.readValue(null);
		this.nextStart = (Date) in.readValue(null);
	}

	public String getChannelKey() {
		return channelKey;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getEpgID() {
		return epgID;
	}

	public String getCurrentProgram() {
		return currentProgram;
	}
	
	public void setEpgData(JSONArray epg)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		Date now = new Date();

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
						nextEnd = df.parse(next.getString("time_end"));
					} catch (Exception e) {
						// fetching the next program failed for some reason
						nextProgram = String.format(App.getContext().getString(R.string.epg_error), e.getMessage());
						nextStart = end;
					}
					epgAge = now;
					break;
				}
			}
		} catch (Exception e) {
			// fetching the next program failed for some reason
			currentProgram = String.format(App.getContext().getString(R.string.epg_error), e.getMessage());
			currentStart = now;
		}
	}
	
	public void setCurrentProgram(String currentProgram) {
		this.currentProgram = currentProgram;
	}

	public String getNextProgram() {
		return nextProgram;
	}

	public void setNextProgram(String nextProgram) {
		this.nextProgram = nextProgram;
	}

	public Date getCurrentStart() {
		return currentStart;
	}

	public void setCurrentStart(Date currentStart) {
		this.currentStart = currentStart;
	}

	public Date getNextStart() {
		return nextStart;
	}

	public void setNextStart(Date nextStart) {
		this.nextStart = nextStart;
	}

	@Override
	public String toString() {
		return channelName;
	}

	public Bitmap getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(Bitmap logoImage) {
		this.logoImage = logoImage;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(channelKey);
		dest.writeString(channelName);
		dest.writeParcelable(logoImage, flags);
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
