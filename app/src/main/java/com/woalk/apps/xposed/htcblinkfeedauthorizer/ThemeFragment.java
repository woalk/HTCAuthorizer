package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ThemeFragment extends PreferenceFragment {

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_themes);

    }

    public void onResume() {
        super.onResume();

    }







}
