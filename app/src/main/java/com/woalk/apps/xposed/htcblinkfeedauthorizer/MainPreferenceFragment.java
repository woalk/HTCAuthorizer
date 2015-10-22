package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {
    public static final String EXTRA_SUBSCREEN_ID = "subscreen_id";
    public static final int SUBSCREEN_ID_ALWAYS_ACTIVE = 1;
    private static Preference pathUSB;
    private static Preference pathExt;
    private static int mToastHitCountdown;
    private File mDefaultDirectory = new File(Environment.getExternalStorageDirectory().toString());
    private Toast toast;


    /**
     * Set a pref_download's summary text to the value it holds.
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
            pref.setSummary(pref.getSharedPreferences().getString(pref.getKey(), ""));
        }
    }

    /**
     * Set all preferences' summary texts to the value the respective pref_download holds.
     * <br/><br/>
     * <b>Works for:</b><br/>
     * <ul>
     * <li>{@link EditTextPreference}</li>
     * </ul>
     */
    public static void setAllPreferenceValuesToSummary() {
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

        addPreferencesFromResource(R.xml.pref_general);
        Preference permpref = findPreference("create_perm");
        Preference killpref = findPreference("kill_launcher");
        if (Common.checkPermFileExists()) {
            mToastHitCountdown = 7;
        } else mToastHitCountdown = 0;
        toast = null;


        pathUSB = findPreference("usb_dir");
        pathExt = findPreference("ext_dir");

        permpref.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Common common;
                        common = new Common(getActivity());
                        Common.createPermFile();

                        final Context context = getActivity();
                        Logger.v("MainPreferenceFragment: Toast count is " + mToastHitCountdown);
                        if (Common.checkPermFileExists()) {
                            Logger.v("MainPreferenceFragment: File exists " + mToastHitCountdown);
                            if (mToastHitCountdown > 0) {
                                mToastHitCountdown -= 1;
                                if (mToastHitCountdown == 0) {
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(getActivity(), "Fiiiiiine.  I'll do it already.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    common.copyPermFile(true);
                                    final Handler handler = new Handler();
                                    Timer t = new Timer();
                                    t.schedule(new TimerTask() {
                                        public void run() {
                                            handler.post(new Runnable() {
                                                public void run() {

                                                    if (Common.checkPermFileExists()) {
                                                        toast = Toast.makeText(context, "File created successfully.", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }

                                                }
                                            });
                                        }
                                    }, 5000);
                                    mToastHitCountdown = 7;

                                } else if ((mToastHitCountdown > 0) && (mToastHitCountdown < 8)) {
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(getActivity(), "File already exists.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        } else {
                            Logger.v("MainPreferenceFragment: File does not exist " + mToastHitCountdown);
                            common.copyPermFile();
                            final Handler handler = new Handler();
                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                public void run() {
                                    handler.post(new Runnable() {
                                        public void run() {

                                            if (Common.checkPermFileExists()) {
                                                toast = Toast.makeText(context, "File created successfully.", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }

                                        }
                                    });
                                }
                            }, 5000);


                            mToastHitCountdown = 7;


                        }

                        return true;
                    }
                });


        killpref.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Common.killPackage("com.htc.launcher");
                        Common.fixPermissions(getActivity());
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        return true;
                    }
                });

        pathUSB.setOnPreferenceClickListener(this);
        pathExt.setOnPreferenceClickListener(this);

        setAllPreferenceValuesToSummary();
    }


    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        final SharedPreferences.Editor editor = preference.getEditor();
        Common.fixPermissions(getActivity());
        File curPath = new File(sharedPreferences.getString(preference.getKey(), mDefaultDirectory.toString()));
        FileDialog fd = new FileDialog(getActivity(), curPath);
        fd.setSelectDirectoryOption(true);
        fd.createFileDialog();
        fd.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            @Override
            public void directorySelected(File directory) {
                Logger.d("MainPrefFrag: Trying to put value of " + directory + " to " + preference.getKey());
                editor.putString(preference.getKey(), directory.toString());
                editor.commit();
                Logger.d("MainPrefFrag: value of " + sharedPreferences.getString(preference.getKey(), ""));
                preference.setSummary(directory.toString());
            }
        });
        return true;

    }


}
