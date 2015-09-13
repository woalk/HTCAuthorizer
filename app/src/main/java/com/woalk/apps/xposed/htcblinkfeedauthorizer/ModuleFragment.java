package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.io.File;

public class ModuleFragment extends PreferenceFragment {

    private static final String KEY_LOG_WARN_SHOWN = "log_warn";
    private String file;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_always_active);
        Preference logpref = findPreference("show_log");
        final Preference logloc = findPreference("log_loc");
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
        logloc.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preferences) {
                        saveLog(logLoc);
                        File sd = Environment.getExternalStorageDirectory();
                        Logger.d("AboutFragment: path " + Uri.fromFile(new File(file)));
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("text/plain");
                        Intent intent = email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
                        startActivity(Intent.createChooser(email, "Email: Text File"));
                        return true;
                    }
                }
        );

    }

    private void saveLog(Preference logLoc) {
        file = Logger.saveLogcat(getActivity());
        logLoc.setEnabled(true);
        logLoc.setSummary("Log file saved to" + file);
    }
}