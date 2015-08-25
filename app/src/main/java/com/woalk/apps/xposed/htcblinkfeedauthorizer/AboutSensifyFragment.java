package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AboutSensifyFragment extends PreferenceFragment {

    private static final String KEY_LOG_WARN_SHOWN = "log_warn";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_always_active);
        Preference logpref = findPreference("show_log");
        Preference exportpref = findPreference("export_log");
        logpref.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getActivity(), LogActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });

        final Preference logLoc = findPreference("log_loc");

        exportpref.setOnPreferenceClickListener(
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

    }

    private void saveLog(Preference logLoc) {
        String file = Logger.saveLogcat(getActivity());
        logLoc.setEnabled(true);
        logLoc.setSummary(String.format(file));
    }
}