package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        picker1 = (XColorPickerPreference) findPreference("theme_PrimaryColor");
        picker2 = (XColorPickerPreference) findPreference("theme_PrimaryDarkColor");
        picker3 = (XColorPickerPreference) findPreference("theme_AccentColor");
        picker4 = (XColorPickerPreference) findPreference("theme_AccentColor");
        Logger.d("ThemeFragment: Oncreate: colors are " + color1 + ", " + color2 + ", " + color3 + ", and " + color4);



    }

    public void onResume() {
        super.onResume();

        //(getActivity());
    }


    public void updateFromXML(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.woalk.apps.xposed.htcblinkfeedauthorizer_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Logger.i("MainPreferenceFragment: Starting Editor");
        mColors = xh.readAllColors();
        color1 = mColors.get(0);
        color2 = mColors.get(1);
        color3 = mColors.get(2);
        Logger.i("MainPreferenceFragment: Colors set to " + color1 + " " + color2 + " " + color3);
        editor.putInt("theme_PrimaryColor", color1);
        editor.putInt("theme_PrimaryDarkColor", color2);
        editor.putInt("theme_AccentColor", color3);

        editor.apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.contains("systemui_color")) {
            //xh.WriteToXML(key, sharedPreferences.getInt(key,2533018));

        }
    }

    public static class ThemeUpdateReceiver extends BroadcastReceiver {
        private int color1, color2, color3, mixcolor;
        public ThemeUpdateReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            // This method is called when this BroadcastReceiver receives an Intent broadcast.
            Bundle extras = intent.getExtras();
            if (intent.getAction().equals("com.woalk.HTCAuthorizer.UPDATE_XML")) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    Logger.d("ThemeFragment: extras are " + String.format("%s %s (%s)", key,
                            value.toString(), value.getClass().getName()));
                }
                if (extras != null) {
                    SharedPreferences sharedPref = context.getSharedPreferences("com.woalk.apps.xposed.htcblinkfeedauthorizer_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (extras.containsKey("theme_PrimaryColor")) {
                        color1 = extras.getInt("theme_PrimaryColor");
                        Logger.d("ThemeFragment: color grabbed from intent " + color1);
                    }
                    if (extras.containsKey("theme_PrimaryDarkColor")) {
                        color2 = extras.getInt("theme_PrimaryDarkColor");
                        Logger.d("ThemeFragment: color grabbed from intent " + color2);

                    }
                    if (extras.containsKey("theme_AccentColor")) {
                        color3 = extras.getInt("theme_AccentColor");
                        Logger.d("ThemeFragment: color grabbed from intent " + color3);
                    }
                    if(sharedPref.getBoolean("systemui_automix_theme",true) && (color1 == color2)) {
                        if (color1 == 0) mixcolor = color3;
                        Logger.d("ThemeFragment: Mixing Colors.  Inputs are " + color1 + " " + color2  + " " + color3);
                        color2 = Common.enlightColor(mixcolor,.4f);
                        color3 = Common.enlightColor(mixcolor,1.0f);
                        color1 = Common.enlightColor(mixcolor,.8f);
                        Logger.d("ThemeFragment: Mixing Colors.  Outputs are " + color1 + " " + color2  + " " + color3);
                    }

                    editor.putInt("theme_PrimaryColor",color1);
                    editor.putInt("theme_PrimaryDarkColor", color2);
                    editor.putInt("theme_AccentColor", color3);
                    editor.apply();
                    Logger.d("ThemeFragment: Intent received");
                }
            }
        }
    }
}
