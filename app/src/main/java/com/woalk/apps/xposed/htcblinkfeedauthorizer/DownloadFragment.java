package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.Locale;

public class DownloadFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference dlPrismPref, dlCameraPref, dlImePref, dlGalleryPref, dlBrowserPref, dlClockPref, dlWeatherPref, dlScribblePref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_download);
        dlPrismPref = findPreference("dl_Prism");
        dlCameraPref = findPreference("dl_Camera");
        dlImePref = findPreference("dl_IME");
        dlClockPref = findPreference("dl_Clock");
        dlWeatherPref = findPreference("dl_Weather");
        dlGalleryPref = findPreference("dl_Gallery");
        dlBrowserPref = findPreference("dl_Browser");
        dlScribblePref = findPreference("dl_Scribble");
        dlPrismPref.setOnPreferenceClickListener(this);
        dlCameraPref.setOnPreferenceClickListener(this);
        dlImePref.setOnPreferenceClickListener(this);
        dlClockPref.setOnPreferenceClickListener(this);
        dlWeatherPref.setOnPreferenceClickListener(this);
        dlGalleryPref.setOnPreferenceClickListener(this);
        dlBrowserPref.setOnPreferenceClickListener(this);
        dlScribblePref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == dlPrismPref) {
            String url = getString(R.string.dl_path_Prism);
            downloadFile(url);
        } else if (preference == dlCameraPref) {
            String url = getString(R.string.dl_path_Camera);
            downloadFile(url);
        } else if (preference == dlGalleryPref) {
            String url = getString(R.string.dl_path_Gallery);
            downloadFile(url);
        } else if (preference == dlImePref) {
            String url = getString(R.string.dl_path_Ime);
            downloadFile(url);
            url = "";
            Locale current = getResources().getConfiguration().locale;
            Logger.d("DownloadFragment: Locale is " + current);
            if (current.toString().equals("en_US")) {
                url = getString(R.string.dl_path_Ime_Eng);
            } else if (current.toString().equals("en_GB")) {
                url = getString(R.string.dl_path_en_GB);
            } else if (current.toString().equals("es_US")) {
                url = getString(R.string.dl_path_Ime_es);
            } else if (current.toString().equals("lv_LV")) {
                url = getString(R.string.dl_path_Ime_lv_LV);
            } else if (current.toString().equals("lt_LT")) {
                url = getString(R.string.dl_path_lt_LT);
            } else if (current.toString().equals("nb_NO")) {
                url = getString(R.string.dl_path_nb_NO);
            } else if (current.toString().equals("it_IT")) {
                url = getString(R.string.dl_path_it_IT);
            } else if (current.toString().equals("ru_RU")) {
                url = getString(R.string.dl_path_ru_RU);
            } else if (current.toString().equals("uk_UA")) {
                url = getString(R.string.dl_path_uk_UA);
            } else if (current.toString().equals("de_DE")) {
                url = getString(R.string.dl_path_de_DE);
            }
            if (!url.equals("")) downloadFile(url);

        } else if (preference == dlClockPref) {
            String url = getString(R.string.dl_path_Clock);
            downloadFile(url);
        } else if (preference == dlWeatherPref) {
            String url = getString(R.string.dl_path_Weather);
            downloadFile(url);
        } else if (preference == dlBrowserPref) {
            String url = getString(R.string.dl_path_Browser);
            downloadFile(url);
        } else if (preference == dlScribblePref) {
            String url = getString(R.string.dl_path_Scribble);
            downloadFile(url);
        }
        return false;
    }

    private void downloadFile(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }
}
