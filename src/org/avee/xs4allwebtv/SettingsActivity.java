package org.avee.xs4allwebtv;

import io.vov.vitamio.LibsChecker;

import org.avee.xs4allwebtv.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(key.equals("useVitamio") && prefs.getBoolean("useVitamio", false)) {
			if (!LibsChecker.checkVitamioLibs(this))
			{
				App.error(R.string.vitamio_failed);
			}
		}
	}
}
