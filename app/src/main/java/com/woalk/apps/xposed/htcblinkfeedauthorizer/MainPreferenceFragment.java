package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

public class MainPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Preference killpref;
    private static Preference pathUSB;
    private static Preference pathExt;

    public static final String EXTRA_SUBSCREEN_ID = "subscreen_id";
    public static final int SUBSCREEN_ID_ALWAYS_ACTIVE = 1;
    private File directory = new File(Environment.getExternalStorageDirectory().toString());


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
        if (pref.getKey().contains("_dir")) {
            pref.setSummary(pref.getSharedPreferences().getString(pref.getKey(),""));
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
                    setPreferenceValueToSummary(pathExt);
                    setPreferenceValueToSummary(pathUSB);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null
                && getArguments().getInt(EXTRA_SUBSCREEN_ID) == SUBSCREEN_ID_ALWAYS_ACTIVE) {
            addPreferencesFromResource(R.xml.pref_always_active);
            return;
        }

        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.pref_general);
        Preference permpref = findPreference("create_perm");
        killpref = findPreference("kill_launcher");

        pathUSB = findPreference("usb_dir");
        pathExt = findPreference("ext_dir");

        permpref.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Common.createPermFile();
                        Common common;
                        common = new Common();
                        if (!common.copyPermFile()) {
                            Toast.makeText(getActivity(), "File already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });


        killpref.setOnPreferenceClickListener(
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

        pathUSB.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(final Preference preference) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        File curPath = new File (sharedPreferences.getString("usb_dir",directory.toString()));
                        FileDialog fd = new FileDialog(getActivity(),curPath);
                        fd.setSelectDirectoryOption(true);
                        fd.createFileDialog();
                        fd.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
                            @Override
                            public void directorySelected(File directory) {
                                preference.getEditor().putString("usb_dir", directory.toString());
                                //preference.setSummary(directory.toString());
                            }
                        });
                        return true;
                    }
                }
        );

        pathExt.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(final Preference preference) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        File curPath = new File (sharedPreferences.getString("ext_dir",directory.toString()));
                        FileDialog fd = new FileDialog(getActivity(),curPath);
                        fd.setSelectDirectoryOption(true);
                        fd.createFileDialog();
                        fd.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
                            @Override
                            public void directorySelected(File directory) {
                                preference.getEditor().putString("ext_dir", directory.toString());
                                //preference.setSummary(directory.toString());
                            }
                        });
                        return true;
                    }
                }
        );

        setAllPreferenceValuesToSummary(getPreferenceScreen());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // update value shown in summary
        Preference pref = findPreference(key);
        if (key.contains("_dir")) {
            pref.setSummary(((EditTextPreference) pref).getText());
        }

    }


}
