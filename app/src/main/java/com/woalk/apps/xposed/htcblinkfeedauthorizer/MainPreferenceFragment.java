package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjithsasindran.materialcolorpicker.ColorPickerActivity;

import java.io.IOException;

public class MainPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String EXTRA_SUBSCREEN_ID = "subscreen_id";
    public static final int SUBSCREEN_ID_ALWAYS_ACTIVE = 1;
    private static final String KEY_LOG_WARN_SHOWN = "log_warn";
    private static final XMLHelper xw = new XMLHelper();
    public Integer color1;
    public Integer color2;
    public Integer color3;
    public Integer color4;
    static final int COLOR_SELECTION_COMPLETE = 1;  // The request code
    static final int COLOR_SELECTION_CANCELLED = 2;  // The request code
    private ImageView iv1;
    private ImageView iv;
    private XColorPickerPreference picker1;
    private XColorPickerPreference picker2;
    private XColorPickerPreference picker3;
    private XColorPickerPreference picker4;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null
                && getArguments().getInt(EXTRA_SUBSCREEN_ID) == SUBSCREEN_ID_ALWAYS_ACTIVE) {
            addPreferencesFromResource(R.xml.pref_always_active);
            return;
        }

        getPreferenceManager().setSharedPreferencesName(SettingsHelper.PREFERENCE_FILE);
        //noinspection deprecation
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.pref_general);
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

        findPreference("always_active").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra(EXTRA_SUBSCREEN_ID, SUBSCREEN_ID_ALWAYS_ACTIVE);
                        startActivity(intent);
                        return true;
                    }
                });

        findPreference("show_log").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), LogActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });

        final Preference logLoc = findPreference("log_loc");

        findPreference("export_log").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (getPreferenceManager().getSharedPreferences()
                                .getBoolean(KEY_LOG_WARN_SHOWN, false)) {
                            saveLog(logLoc);
                            return true;
                        }
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.warn_logs_title)
                                .setMessage(R.string.warn_logs_content)
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getPreferenceManager().getSharedPreferences().edit()
                                                        .putBoolean(KEY_LOG_WARN_SHOWN, true)
                                                        .apply();
                                                saveLog(logLoc);
                                            }
                                        })
                                .create()
                                .show();
                        return true;
                    }
                });

        findPreference("kill_launcher").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Common.killPackage("com.htc.launcher");
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        return true;
                    }
                });

        try {
            findPreference("version").setSummary(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            findPreference("version").setSummary("ERROR");
            e.printStackTrace();
        }

        setAllPreferenceValuesToSummary(getPreferenceScreen());
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // update value shown in summary
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
        }

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

    /**
     * Set a preference's summary text to the value it holds.
     * <br/><br/>
     * <b>Works for:</b><br/>
     * <ul>
     * <li>{@link EditTextPreference}</li>
     * </ul>
     *
     * @param pref The {@link Preference} to check and edit, if possible.
     */
    public static void setPreferenceValueToSummary(Preference pref) {
        if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
        }
    }

    /**
     * Set all preferences' summary texts to the value the respective preference holds.
     * <br/><br/>
     * <b>Works for:</b><br/>
     * <ul>
     * <li>{@link EditTextPreference}</li>
     * </ul>
     *
     * @param prefG The {@link PreferenceGroup} to iterate over, check and edit, if possible.
     */
    public static void setAllPreferenceValuesToSummary(PreferenceGroup prefG) {
        for (int i = 0; i < prefG.getPreferenceCount(); i++) {
            Preference pref = prefG.getPreference(i);
            if (pref instanceof PreferenceGroup) {
                setAllPreferenceValuesToSummary((PreferenceGroup) pref);
            } else {
                setPreferenceValueToSummary(pref);
            }
        }
    }

    private void saveLog(Preference logLoc) {
        String file = Logger.saveLogcat(getActivity());
        logLoc.setEnabled(true);
        logLoc.setSummary(String.format(
                getString(R.string.pref_debug_export_log_toast), file));
    }


}
