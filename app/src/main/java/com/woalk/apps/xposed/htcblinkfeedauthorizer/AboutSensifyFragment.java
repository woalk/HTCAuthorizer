package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.woalk.apps.xposed.htcblinkfeedauthorizer.R;

public class AboutSensifyFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_always_active);
    }
}