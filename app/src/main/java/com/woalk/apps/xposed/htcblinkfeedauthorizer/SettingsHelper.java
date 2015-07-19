package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "main";

    private final XSharedPreferences mPref;

    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);
    }

    public boolean getPref_has_ext() {
        return mPref.getBoolean("has_ext", false);
    }

    public String getPref_ext_path() {
        return mPref.getString("ext_path", "/storage/ext_sd");
    }

    public boolean getPref_has_usb() {
        return mPref.getBoolean("has_usb", false);
    }

    public String getPref_usb_path() {
        return mPref.getString("usb_path", "/storage/usb");
    }
}
