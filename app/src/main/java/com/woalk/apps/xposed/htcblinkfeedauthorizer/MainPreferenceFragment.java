package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import java.io.DataOutputStream;
import java.io.IOException;
public class MainPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String EXTRA_SUBSCREEN_ID = "subscreen_id";
    public static final int SUBSCREEN_ID_ALWAYS_ACTIVE = 1;

    @Override
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

        try {
            findPreference("version").setSummary(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            findPreference("version").setSummary("ERROR");
            e.printStackTrace();
        }

        setAllPreferenceValuesToSummary(getPreferenceScreen());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // update value shown in summary
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
        } else if (key.contains("force_rotate")) {
            killPackage("com.htc.launcher");
        }
    }
    private void killPackage(String packageToKill) {

        Process su = null;

        // get superuser
        try {

            su = Runtime.getRuntime().exec("su");

        } catch (IOException e) {

            e.printStackTrace();

        }

        // kill given package
        if (su != null ){

            try {

                DataOutputStream os = new DataOutputStream(su.getOutputStream());
                os.writeBytes("pkill " + packageToKill + "\n");
                os.flush();
                os.writeBytes("exit\n");
                os.flush();
                su.waitFor();

            } catch (IOException e) {

                e.printStackTrace();

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
        }


    }
    /**
     * Set a preference's summary text to the value it holds.
     * <br/><br/>
     * <b>Works for:</b><br/>
     * <ul>
     *     <li>{@link EditTextPreference}</li>
     * </ul>
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
     *     <li>{@link EditTextPreference}</li>
     * </ul>
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
}
