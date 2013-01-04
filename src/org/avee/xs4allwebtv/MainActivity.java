package org.avee.xs4allwebtv;

import org.avee.xs4allwebtv.tasks.FetchChannelsTask;
import org.avee.xs4allwebtv.tasks.OpenStreamTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        ListView listView = (ListView) findViewById(R.id.listChannels);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                ChannelInfo channel = (ChannelInfo) parent.getAdapter().getItem(position);
              	new OpenStreamTask(MainActivity.this).execute(channel);
            }
        });
        
        if(savedInstanceState != null) {
        	try {
	        	ChannelInfo[] channels = (ChannelInfo[]) savedInstanceState.getParcelableArray("channels");
	        	if(channels != null) {
		        	ChannelAdapter adapter = new ChannelAdapter(this, channels);
		        	listView.setAdapter(adapter);
		        	
		        	// Restored state, so we're done.
		        	return;
	        	}
        	} catch(Exception e) {
    			// Never crash while saving.
    			Log.e("org.avee.xs4allwebtv.MainActivity", "Error restoring state", e);
    		}
        }

        // Didn't restore, fetch...
        new FetchChannelsTask(this).execute();
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
	        ListView listView = (ListView) findViewById(R.id.listChannels);
	        ChannelAdapter apdapter = (ChannelAdapter) listView.getAdapter();
	        ChannelInfo[] channels = apdapter.getChannels();
			outState.putParcelableArray("channels", channels);
		} catch(Exception e) {
			// Never crash while saving.
			Log.e("org.avee.xs4allwebtv.MainActivity", "Error saving state", e);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
