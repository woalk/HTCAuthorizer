package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class DownloadFragment extends PreferenceFragment {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_download);
        Common.fixPermissions(getActivity());

    }

    @Override
    public Preference findPreference(CharSequence key) {
        return super.findPreference(key);
    }


    public void queryAllPackages() {

        PreferenceScreen ps = getPreferenceScreen();
        for (int i = 0; i < ps.getPreferenceCount(); i++) {
            DownloadPreference pref = (DownloadPreference) ps.getPreference(i);
            pref.QuerySelf();
            pref.RefreshPreferenceSummary();


        }

    }

}
