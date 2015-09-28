package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.Locale;

public class DownloadFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private Preference dlCameraPref, dlImePref, dlGalleryPref, dlBrowserPref, dlClockPref, dlWeatherPref, dlScribblePref;
    private HTMLHelper htmlHelper;
    private String secondaryApp;
    private Boolean clockInstalled, weatherInstalled, cameraInstalled, prismInstalled;
    private Boolean scribbleInstalled, imeInstalled, galleryInstalled, browserInstalled;
    private String versionClock, versionWeather, versionCamera, versionPrism, versionScribble;
    private String versionIme, versionGallery, versionBrowser, queriedVersion;
    private SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_download);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        dlCameraPref = findPreference("dl_Camera");
        dlImePref = findPreference("dl_IME");
        dlClockPref = findPreference("dl_Clock");
        dlWeatherPref = findPreference("dl_Weather");
        dlGalleryPref = findPreference("dl_Gallery");
        dlBrowserPref = findPreference("dl_Browser");
        dlScribblePref = findPreference("dl_Scribble");
        dlCameraPref.setOnPreferenceClickListener(this);
        dlImePref.setOnPreferenceClickListener(this);
        dlClockPref.setOnPreferenceClickListener(this);
        dlWeatherPref.setOnPreferenceClickListener(this);
        dlGalleryPref.setOnPreferenceClickListener(this);
        dlBrowserPref.setOnPreferenceClickListener(this);
        dlScribblePref.setOnPreferenceClickListener(this);
        queryAllPackages();





    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        queryAllPackages();

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    private String queryPackage(String packageName, Preference preference) {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            Logger.d("DownloadFragment: version code found of " + packageInfo.versionName);
            preference.setSummary("Installed Version: " + packageInfo.versionName);
            queriedVersion = packageInfo.versionName;
            return packageInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {

            Logger.d("DownloadFragment: No package found");
            return "";
        }

    }

    private void queryAllPackages() {

        clockInstalled = !(queryPackage("com.htc.android.worldclock",dlClockPref).equals(""));
        if (clockInstalled) {versionClock = queriedVersion;}
        weatherInstalled = !(queryPackage("com.htc.weather",dlWeatherPref).equals(""));
        if (weatherInstalled) {versionWeather = queriedVersion;}
        cameraInstalled = !(queryPackage("com.htc.camera",dlCameraPref).equals(""));
        if (cameraInstalled) {versionCamera = queriedVersion;}
        galleryInstalled = !(queryPackage("com.htc.album",dlGalleryPref).equals(""));
        if (galleryInstalled) {versionGallery = queriedVersion;}
        browserInstalled = !(queryPackage("com.htc.sense.internet",dlBrowserPref).equals(""));
        if (browserInstalled) {versionBrowser = queriedVersion;}
        scribbleInstalled = !(queryPackage("com.htc.lucy",dlScribblePref).equals(""));
        if (scribbleInstalled) {versionScribble = queriedVersion;}
        imeInstalled = !(queryPackage("com.htc.ime",dlImePref).equals(""));
        if (imeInstalled) {versionIme = queriedVersion;}
    }



    @Override
    public boolean onPreferenceClick(Preference preference) {
        htmlHelper = new HTMLHelper();


        if (preference == dlImePref) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HTMLHelper htmlHelper1 = new HTMLHelper();
                    Preference preference1 = new Preference(getActivity());

                    htmlHelper1.fetchApp("HTC Input", getActivity(), 1, preference1);
                }
            }, 2000);
            Locale current = getResources().getConfiguration().locale;
            Logger.d("DownloadFragment: Locale is " + current);
            if (current.toString().equals("en_US")) {
                htmlHelper.fetchApp("Keyboard - English Pack with ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("es_US")) {
                htmlHelper.fetchApp("Keyboard Spanish ALM", getActivity(), 0, preference);
            } else if (current.toString().contains("en_")) {
                htmlHelper.fetchApp("Keyboard English_UK ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("lv_LV")) {
                htmlHelper.fetchApp("Keyboard Latvian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("lt_LT")) {
                htmlHelper.fetchApp("Keyboard Lithuanian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("nb_NO")) {
                htmlHelper.fetchApp("Keyboard Norwegian ALM", getActivity(), 0, preference);
            } else if ((current.toString().equals("it_IT")) || (current.toString().equals("it_CH"))) {
                htmlHelper.fetchApp("Keyboard Italian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("ru_RU")) {
                htmlHelper.fetchApp("Keyboard Russian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("uk_UA")) {
                htmlHelper.fetchApp("Keyboard Ukranian ALM", getActivity(), 0, preference);
            } else if (current.toString().contains("de_")) {
                htmlHelper.fetchApp("Keyboard German ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("cs_CZ")) {
                htmlHelper.fetchApp("Keyboard Czech ALM", getActivity(), 0, preference);
            } else if ((current.toString().equals("nl_BE")) || (current.toString().equals("nl_NL"))) {
                htmlHelper.fetchApp("Keyboard Dutch ALM", getActivity(), 0, preference);
            } else if (current.toString().contains("fr_")) {
                htmlHelper.fetchApp("Keyboard French ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("pl_PL")) {
                htmlHelper.fetchApp("Keyboard Polish ALM", getActivity(), 0, preference);
            } else if (current.toString().contains("ar_")) {
                htmlHelper.fetchApp("Keyboard Arabic ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("es_ES")) {
                htmlHelper.fetchApp("Keyboard Polish ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("bg_BG")) {
                htmlHelper.fetchApp("Keyboard Bulgarian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("ca_ES")) {
                htmlHelper.fetchApp("Keyboard Catalan ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("hr_HR")) {
                htmlHelper.fetchApp("Keyboard Croatian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("da_DK")) {
                htmlHelper.fetchApp("Keyboard Danish ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("el_GR")) {
                htmlHelper.fetchApp("Keyboard Greek ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("iw_IL")) {
                htmlHelper.fetchApp("Keyboard Hebrew ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("in_ID")) {
                htmlHelper.fetchApp("Keyboard Indonesian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("bg_BG")) {
                htmlHelper.fetchApp("Keyboard Bulgarian ALM", getActivity(), 0, preference);
            } else if (current.toString().contains("pt_")) {
                htmlHelper.fetchApp("Keyboard Portuguese ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("ro_RO")) {
                htmlHelper.fetchApp("Keyboard Romanian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("sr_RS")) {
                htmlHelper.fetchApp("Keyboard Serbian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("sk_SK")) {
                htmlHelper.fetchApp("Keyboard Slovakian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("sl_SI")) {
                htmlHelper.fetchApp("Keyboard Slovenian ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("sv_SE")) {
                htmlHelper.fetchApp("Keyboard Swedish ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("th_TH")) {
                htmlHelper.fetchApp("Keyboard Thai ALM", getActivity(), 0, preference);
            } else if (current.toString().equals("tr_RT")) {
                htmlHelper.fetchApp("Keyboard Turkish ALM", getActivity(), 0, preference);
            }

        } else {
            String prefTitle = (String) preference.getTitle();
            preference.getExtras();
            htmlHelper.fetchApp(prefTitle, getActivity(), 0, preference);

//        } else if (preference == dlCameraPref) {
//            String versionInfo = queryPackage("com.htc.camera",preference);
//            if (versionInfo.equals("")) {
//                htmlHelper.fetchApp("HTC Camera", getActivity(), 4, preference);
//            }
//        } else if (preference == dlGalleryPref) {
//            htmlHelper.fetchApp("HTC Gallery", getActivity(), 5, preference);
//
//        } else if (preference == dlClockPref) {
//            htmlHelper.fetchApp("HTC Clock", getActivity(), 6, preference);
//
//
//        } else if (preference == dlWeatherPref) {
//            htmlHelper.fetchApp("HTC Weather",getActivity(), 7, preference);
//        } else if (preference == dlBrowserPref) {
//            String versionInfo = queryPackage("com.htc.sense.browser", preference);
//            if (!versionInfo.equals(sharedPreferences.getString(preference.getKey(),"1"))) {
//                htmlHelper.fetchApp("HTC Internet",getActivity(), 8, preference);
//            } else {
//                Toast toast = Toast.makeText(getActivity(),"Latest package already installed", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//
//        } else if (preference == dlScribblePref) {
//            htmlHelper.fetchApp("HTC Scribble",getActivity(), 9, preference);
//        }
        }
        return false;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.contains("dl")) {
            findPreference(key).setSummary("Installed Version: " + sharedPreferences.getString(key,""));
        }

    }

}
