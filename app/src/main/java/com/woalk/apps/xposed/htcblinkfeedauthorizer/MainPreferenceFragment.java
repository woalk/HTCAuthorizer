package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MainPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
    }
}
