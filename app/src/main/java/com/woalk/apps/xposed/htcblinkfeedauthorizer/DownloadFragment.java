package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

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
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581";
            downloadFile(url);
            //downloadApp.cancel(true);
            //downloadApp.execute("http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581");
        } else if (preference == dlCameraPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=18737";
            downloadFile(url);
        } else if (preference == dlGalleryPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581";
            downloadFile(url);
        } else if (preference == dlImePref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=16867";
            downloadFile(url);
            url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=16106";
            downloadFile(url);
        } else if (preference == dlClockPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581";
            downloadFile(url);
        } else if (preference == dlWeatherPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581";
            downloadFile(url);
        } else if (preference == dlGalleryPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=16517";
            downloadFile(url);
        } else if (preference == dlBrowserPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=18733";
            downloadFile(url);
        } else if (preference == dlPrismPref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21581";
            downloadFile(url);
            url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=21223";
            downloadFile(url);
        } else if (preference == dlScribblePref) {
            String url = "http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=16625";
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
