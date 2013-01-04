package org.avee.xs4allwebtv;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ChannelInfo implements Parcelable {

	private String channelKey;
	private String channelName;

	private String currentProgram;
	private String nextProgram;

	private Bitmap logoImage;

	public ChannelInfo(String key, String name) {
		this.channelKey = key;
		this.channelName = name;
		this.currentProgram = "Geen gidsinformatie beschikbaar";
		this.nextProgram = "Geen gidsinformatie beschikbaar";
	}
	
	public ChannelInfo(Parcel in) {
		this.channelKey = in.readString();
		this.channelName = "* " + in.readString();
		this.logoImage = in.readParcelable(null);
		this.currentProgram = in.readString();
		this.nextProgram = in.readString();
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

	public void setCurrentProgram(String currentProgram) {
		this.currentProgram = currentProgram;
	}

	public String getNextProgram() {
		return nextProgram;
	}

	public void setNextProgram(String nextProgram) {
		this.nextProgram = nextProgram;
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
