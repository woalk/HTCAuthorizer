package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.anjithsasindran.materialcolorpicker.ColorPickerActivity;

import java.io.IOException;

/**
 * Created by Ben on 8/23/2015.
 */
public class ThemeFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final XMLHelper xw = new XMLHelper();
    public Integer color1;
    public Integer color2;
    public Integer color3;
    public Integer color4;
    static final int COLOR_SELECTION_COMPLETE = 1;  // The request code
    static final int COLOR_SELECTION_CANCELLED = 2;  // The request code
    private XColorPickerPreference picker1;
    private XColorPickerPreference picker2;
    private XColorPickerPreference picker3;
    private XColorPickerPreference picker4;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_themes);
        picker1 = (XColorPickerPreference) findPreference(getString(R.string.CP1));
        picker2 = (XColorPickerPreference) findPreference(getString(R.string.CP2));
        picker3 = (XColorPickerPreference) findPreference(getString(R.string.CP3));
        picker4 = (XColorPickerPreference) findPreference(getString(R.string.CP4));
        Logger.d("MainPreferenceFragment: Oncreate: colors are " + color1 + ", " + color2 + ", " + color3 + ", and " + color4);


        picker1.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
                        intent.putExtra("Prefname", "systemui_color1");
                        try {
                            intent.putExtra("Current", xw.readFromXML(0));
                            Logger.d("MainPref: Passing sui1 color of " + xw.readFromXML(0));
                            Logger.d("MainPref: Alt. value is color of " + color1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, COLOR_SELECTION_COMPLETE);
                        return true;
                    }
                });
        picker2.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
                        intent.putExtra("Prefname", "systemui_color2");
                        try {
                            intent.putExtra("Current", xw.readFromXML(1));
                            Logger.d("MainPref: Passing sui2 color of " + xw.readFromXML(1));
                            Logger.d("MainPref: Alt. value is color of " + color2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, COLOR_SELECTION_COMPLETE);
                        return true;
                    }
                });
        picker3.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
                        intent.putExtra("Prefname", "systemui_color3");
                        try {
                            intent.putExtra("Current", xw.readFromXML(2));
                            Logger.d("MainPref: Passing sui3 color of " + xw.readFromXML(2));
                            Logger.d("MainPref: Alt. value is color of " + color3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, COLOR_SELECTION_COMPLETE);
                        return true;
                    }
                });
        picker4.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
                        intent.putExtra("Prefname", "systemui_color4");
                        try {
                            intent.putExtra("Current", xw.readFromXML(3));
                            Logger.d("MainPref: Passing sui4 color of " + xw.readFromXML(3));
                            Logger.d("MainPref: Alt. value is color of " + color4);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, COLOR_SELECTION_COMPLETE);
                        return true;
                    }
                });

    }

    public void onResume() {
        super.onResume();
        updateFromXML(getActivity());
    }

    private void updateViews() {
        picker1.setMyColor(color1);
        picker2.setMyColor(color2);
        picker3.setMyColor(color3);
        picker4.setMyColor(color4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Logger.d("MainPreferenceFragment: Result received");
        if (requestCode == COLOR_SELECTION_COMPLETE) {
            if (resultCode == COLOR_SELECTION_COMPLETE) {
                Bundle res = data.getExtras();
                String colorname = res.getString("Name");
                Integer result = res.getInt("Color");
                Logger.d("MainPreferenceFragment: color data for " + colorname + " " + result);
                Toast.makeText(getActivity(), "Color for " + colorname + " " + result + " has been saved.", Toast.LENGTH_SHORT).show();
                xw.WriteToXML(colorname, result);
                updateFromXML(getActivity());
            } else if (resultCode == COLOR_SELECTION_CANCELLED) {
                Logger.d("MainPreferenceFragment: color selection cancelled");
            }
        }
    }

    public void updateFromXML(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Logger.i("MainPreferenceFragment: Starting Editor");

        try {
            color1 = xw.readFromXML(0);
            color2 = xw.readFromXML(1);
            color3 = xw.readFromXML(2);
            color4 = xw.readFromXML(3);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("Error reading from file" + e);
        }
        Logger.i("MainPreferenceFragment: Colors set to " + color1 + " " + color2 + " " + color3 + " " + color4);
        editor.putInt("systemui_color1", color1);
        editor.putInt("systemui_color2", color2);
        editor.putInt("systemui_color3", color3);
        editor.putInt("systemui_color4", color4);
        updateViews();
        editor.apply();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
