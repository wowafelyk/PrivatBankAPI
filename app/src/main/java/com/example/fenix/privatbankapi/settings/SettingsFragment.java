package com.example.fenix.privatbankapi.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.fenix.privatbankapi.R;

/**
 * Created by fenix on 18.08.2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

}
