package org.avee.xs4allwebtv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {
	private ArrayList<ChannelInfo> channels;
	Context context;
	
	public ChannelAdapter(Context context, ArrayList<ChannelInfo> channels) {
		this.channels = channels;
		this.context = context;
	}

	@Override
	public int getCount() {
		return getChannels().size();
	}

	@Override
	public Object getItem(int position) {
		return getChannels().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = convertView;
		if(result == null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = vi.inflate(R.layout.channel_row, parent, false);
		}
		ChannelInfo channel = getChannels().get(position);
		TextView txtName = (TextView) result.findViewById(R.id.txtChannelName);
		txtName.setText(channel.getChannelName());	
		ImageView img = (ImageView) result.findViewById(R.id.imgChannelLogo);
		img.setImageBitmap(channel.getLogoImage());
		TextView txtCurrentProgram = (TextView) result.findViewById(R.id.txtCurrentProgram);
		TextView txtNextProgram = (TextView) result.findViewById(R.id.txtNextProgram);
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
			String currentText = df.format(channel.getCurrentStart()) + " " + channel.getCurrentProgram();
			String nextText = df.format(channel.getNextStart()) + " " + channel.getNextProgram();
			txtCurrentProgram.setText(currentText);
			txtNextProgram.setText(nextText);
		} catch (Exception e) {
			txtCurrentProgram.setText(e.getMessage());
			txtNextProgram.setText(R.string.no_epg_data);
		}	
		
		return result;
	}

	public ArrayList<ChannelInfo> getChannels() {
		return channels;
	}
}
