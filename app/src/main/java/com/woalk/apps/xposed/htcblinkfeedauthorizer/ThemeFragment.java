package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;

public class ThemeFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private String colorString;
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
        colorArray[9] = sharedPref.getInt("theme_Color4", 0);
        colorArray[10] = sharedPref.getInt("theme_Entertainment_Light", 0);
        colorArray[11] = sharedPref.getInt("theme_Entertainment_Dark", 0);

        rewriteColorIni(colorArray);

        Intent intent = new Intent("com.htc.themepicker.ACTION_THEME_SELECTOR");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("update_colors", colorArray);
        intent.putExtra("colorParam", colorString);
        startActivity(intent);
        return false;
    }

    private void rewriteColorIni(int[] Colors) {
        String hexColor0 = "ff" + Integer.toHexString(Colors[0]).substring(2);
        String hexColor1 = "ff" + Integer.toHexString(Colors[1]).substring(2);
        String hexColor2 = "ff" + Integer.toHexString(Colors[2]).substring(2);
        String hexColor3 = "ff" + Integer.toHexString(Colors[3]).substring(2);
        String hexColor4 = "ff" + Integer.toHexString(Colors[4]).substring(2);
        String hexColor5 = "ff" + Integer.toHexString(Colors[5]).substring(2);
        String hexColor6 = "ff" + Integer.toHexString(Colors[6]).substring(2);
        String hexColor7 = "ff" + Integer.toHexString(Colors[7]).substring(2);
        String hexColor8 = "ff" + Integer.toHexString(Colors[8]).substring(2);
        String hexColor9 = "ff" + Integer.toHexString(Colors[9]).substring(2);
        String hexColor10 = "ff" + Integer.toHexString(Colors[10]).substring(2);
        String hexColor11 = "ff" + Integer.toHexString(Colors[11]).substring(2);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long epoch = date.getTime();
        colorString = "C:" + hexColor0 + ":" + hexColor1  + ":" + hexColor2  + ":" + hexColor3 + ":" + hexColor4 + ":" + hexColor5 + ":" + hexColor6 + ":" + hexColor7 + ":" + hexColor8 + ":" + hexColor9 + ":" + hexColor10 + ":" + hexColor11 + "#:time=" + epoch + ":isFile=true" ;



    }
}


