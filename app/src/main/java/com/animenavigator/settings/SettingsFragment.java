package com.animenavigator.settings;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import com.animenavigator.R;

/**
 * Created by a.g.seliverstov on 14.04.2016.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

}