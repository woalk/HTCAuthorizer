package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

public class MainPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(SettingsHelper.PREFERENCE_FILE);
        //noinspection deprecation
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.pref_general);

        setAllPreferenceValuesToSummary(getPreferenceScreen());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // update value shown in summary
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
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
