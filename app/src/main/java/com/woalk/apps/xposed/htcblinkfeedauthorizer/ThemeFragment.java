package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

public class ThemeFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public XMLHelper xh;
    ArrayList<Integer> mColors = new ArrayList<>();
    private int color1, color2, color3, color4;
    private XColorPickerPreference picker1;
    private XColorPickerPreference picker2;
    private XColorPickerPreference picker3;
    private XColorPickerPreference picker4;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        xh = new XMLHelper();
        addPreferencesFromResource(R.xml.pref_themes);
        picker1 = (XColorPickerPreference) findPreference("systemui_color1");
        picker2 = (XColorPickerPreference) findPreference("systemui_color2");
        picker3 = (XColorPickerPreference) findPreference("systemui_color3");
        picker4 = (XColorPickerPreference) findPreference("systemui_color4");
        Logger.d("ThemeFragment: Oncreate: colors are " + color1 + ", " + color2 + ", " + color3 + ", and " + color4);



    }

    public void onResume() {
        super.onResume();
        updateFromXML(getActivity());
    }


    public void updateFromXML(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.woalk.apps.xposed.htcblinkfeedauthorizer_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Logger.i("MainPreferenceFragment: Starting Editor");
        mColors = xh.readAllColors();
        color1 = mColors.get(0);
        color2 = mColors.get(1);
        color3 = mColors.get(2);
        color4 = mColors.get(3);
        Logger.i("MainPreferenceFragment: Colors set to " + color1 + " " + color2 + " " + color3 + " " + color4);
        editor.putInt("systemui_color1", color1);
        editor.putInt("systemui_color2", color2);
        editor.putInt("systemui_color3", color3);
        editor.putInt("systemui_color4", color4);
//        updateViews();
        editor.apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.contains("systemui_color")) {
            xh.WriteToXML(key, sharedPreferences.getInt(key,2533018));

        }
    }
}
