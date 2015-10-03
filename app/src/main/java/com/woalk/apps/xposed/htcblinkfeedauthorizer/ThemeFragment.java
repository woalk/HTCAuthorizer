package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class ThemeFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_themes);
        Preference buttonPreference = this.findPreference("apply_theme");
        buttonPreference.setOnPreferenceClickListener(this);


    }

    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Logger.d("MainActivity: We got something, Jim.");
        int[] colorArray = new int[12];
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        colorArray[0] = sharedPref.getInt("theme_PrimaryColor", 0);
        colorArray[1] = sharedPref.getInt("theme_AccentColor", 0);
        colorArray[2] = sharedPref.getInt("theme_PrimaryDarkColor", 0);
        colorArray[3] = sharedPref.getInt("theme_Comms_Primary", 0);
        colorArray[4] = sharedPref.getInt("theme_Comms_Light", 0);
        colorArray[5] = sharedPref.getInt("theme_Comms_Dark", 0);
        colorArray[6] = sharedPref.getInt("theme_Info_Primary", 0);
        colorArray[7] = sharedPref.getInt("theme_Info_Light", 0);
        colorArray[8] = sharedPref.getInt("theme_Info_Dark", 0);
        colorArray[9] = sharedPref.getInt("theme_Entertainment_Primary", 0);
        colorArray[10] = sharedPref.getInt("theme_Entertainment_Light", 0);
        colorArray[11] = sharedPref.getInt("theme_Entertainment_Dark", 0);

        Intent intent = new Intent("com.htc.themepicker.ACTION_THEME_SELECTOR");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("update_colors", colorArray);
        //intent.setComponent(new ComponentName("com.htc.launcher", "com.htc.themepicker.thememaker.ColorsActivity"));
        startActivity(intent);

        return false;
    }
}


