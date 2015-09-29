package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class DownloadFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_download);


        PreferenceScreen ps = getPreferenceScreen();

    }

    @Override
    public Preference findPreference(CharSequence key) {
        return super.findPreference(key);
    }

    private String queryPackage(String packageName, Preference preference) {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            preference.setSummary("Installed Version: " + packageInfo.versionName);
            return packageInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }

    }

    public void queryAllPackages() {

        PreferenceScreen ps = getPreferenceScreen();
        for (int i=0; i < ps.getPreferenceCount(); i++) {
            DownloadPreference pref = (DownloadPreference) ps.getPreference(i);
            queryPackage(DownloadPreference.mPackageName,pref);
            HTMLHelper htmlHelper = new HTMLHelper();
            htmlHelper.fetchApp((String) pref.getTitle(),getActivity(),0,pref.getKey(),true);

        }

    }

}
