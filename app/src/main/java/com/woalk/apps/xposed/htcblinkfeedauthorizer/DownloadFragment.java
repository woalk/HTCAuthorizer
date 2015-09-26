package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.Locale;

public class DownloadFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference dlPrismPref, dlCameraPref, dlImePref, dlGalleryPref, dlBrowserPref, dlClockPref, dlWeatherPref, dlScribblePref;
    private HTMLHelper htmlHelper;
    private String secondaryApp;

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
        htmlHelper = new HTMLHelper();
        if (preference == dlPrismPref) {
            htmlHelper.fetchApp("Sense Home", getActivity());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HTMLHelper htmlHelper1 = new HTMLHelper();

                    htmlHelper1.fetchApp("Htc Account", getActivity());
                }
            }, 2000);

        } else if (preference == dlCameraPref) {
            htmlHelper.fetchApp("HTC Camera", getActivity());
        } else if (preference == dlGalleryPref) {
            htmlHelper.fetchApp("HTC Gallery", getActivity());
        } else if (preference == dlImePref) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HTMLHelper htmlHelper1 = new HTMLHelper();

                    htmlHelper1.fetchApp("HTC Input", getActivity());
                }
            }, 2000);
            Locale current = getResources().getConfiguration().locale;
            Logger.d("DownloadFragment: Locale is " + current);
            if (current.toString().equals("en_US")) {
                htmlHelper.fetchApp("Keyboard - English Pack with ALM", getActivity());
            } else if (current.toString().equals("es_US")) {
                htmlHelper.fetchApp("Keyboard Spanish ALM", getActivity());
            } else if (current.toString().contains("en_")) {
                htmlHelper.fetchApp("Keyboard English_UK ALM", getActivity());
            } else if (current.toString().equals("lv_LV")) {
                htmlHelper.fetchApp("Keyboard Latvian ALM", getActivity());
            } else if (current.toString().equals("lt_LT")) {
                htmlHelper.fetchApp("Keyboard Lithuanian ALM", getActivity());
            } else if (current.toString().equals("nb_NO")) {
                htmlHelper.fetchApp("Keyboard Norwegian ALM", getActivity());
            } else if ((current.toString().equals("it_IT")) || (current.toString().equals("it_CH"))) {
                htmlHelper.fetchApp("Keyboard Italian ALM", getActivity());
            } else if (current.toString().equals("ru_RU")) {
                htmlHelper.fetchApp("Keyboard Russian ALM", getActivity());
            } else if (current.toString().equals("uk_UA")) {
                htmlHelper.fetchApp("Keyboard Ukranian ALM", getActivity());
            } else if (current.toString().contains("de_")) {
                htmlHelper.fetchApp("Keyboard German ALM", getActivity());
            } else if (current.toString().equals("cs_CZ")) {
                htmlHelper.fetchApp("Keyboard Czech ALM", getActivity());
            } else if ((current.toString().equals("nl_BE")) || (current.toString().equals("nl_NL"))) {
                htmlHelper.fetchApp("Keyboard Dutch ALM", getActivity());
            } else if (current.toString().contains("fr_")) {
                htmlHelper.fetchApp("Keyboard French ALM", getActivity());
            } else if (current.toString().equals("pl_PL")) {
                htmlHelper.fetchApp("Keyboard Polish ALM", getActivity());
            } else if (current.toString().contains("ar_")) {
                htmlHelper.fetchApp("Keyboard Arabic ALM", getActivity());
            } else if (current.toString().equals("es_ES")) {
                htmlHelper.fetchApp("Keyboard Polish ALM", getActivity());
            } else if (current.toString().equals("bg_BG")) {
                htmlHelper.fetchApp("Keyboard Bulgarian ALM", getActivity());
            } else if (current.toString().equals("ca_ES")) {
                htmlHelper.fetchApp("Keyboard Catalan ALM", getActivity());
            } else if (current.toString().equals("hr_HR")) {
                htmlHelper.fetchApp("Keyboard Croatian ALM", getActivity());
            } else if (current.toString().equals("da_DK")) {
                htmlHelper.fetchApp("Keyboard Danish ALM", getActivity());
            } else if (current.toString().equals("el_GR")) {
                htmlHelper.fetchApp("Keyboard Greek ALM", getActivity());
            } else if (current.toString().equals("iw_IL")) {
                htmlHelper.fetchApp("Keyboard Hebrew ALM", getActivity());
            } else if (current.toString().equals("in_ID")) {
                htmlHelper.fetchApp("Keyboard Indonesian ALM", getActivity());
            } else if (current.toString().equals("bg_BG")) {
                htmlHelper.fetchApp("Keyboard Bulgarian ALM", getActivity());
            } else if (current.toString().contains("pt_")) {
                htmlHelper.fetchApp("Keyboard Portuguese ALM", getActivity());
            } else if (current.toString().equals("ro_RO")) {
                htmlHelper.fetchApp("Keyboard Romanian ALM", getActivity());
            } else if (current.toString().equals("sr_RS")) {
                htmlHelper.fetchApp("Keyboard Serbian ALM", getActivity());
            } else if (current.toString().equals("sk_SK")) {
                htmlHelper.fetchApp("Keyboard Slovakian ALM", getActivity());
            } else if (current.toString().equals("sl_SI")) {
                htmlHelper.fetchApp("Keyboard Slovenian ALM", getActivity());
            } else if (current.toString().equals("sv_SE")) {
                htmlHelper.fetchApp("Keyboard Swedish ALM", getActivity());
            } else if (current.toString().equals("th_TH")) {
                htmlHelper.fetchApp("Keyboard Thai ALM", getActivity());
            } else if (current.toString().equals("tr_RT")) {
                htmlHelper.fetchApp("Keyboard Turkish ALM", getActivity());
            }


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
