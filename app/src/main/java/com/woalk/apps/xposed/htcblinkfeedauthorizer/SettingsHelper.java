package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.SharedPreferences;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = PACKAGE_NAME + "_preferences";

    private final XSharedPreferences mPref;
    private SharedPreferences sharedPreferences;

    private boolean cachedPref_use_themes;
    private int mPrimaryColor, mPrimaryDarkColor, mAccentColor;


    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);
        mPref.reload();
        loadCachePrefs();
    }

    public void loadCachePrefs() {
        cachedPref_use_themes = getPref_use_themes();
        getColorPrimary();
        getColorPrimaryDark();
        getColorAccent();

    }

    protected boolean getPref_use_themes() {
        return mPref.getBoolean("use_themes",false);

    }

    public boolean getCachedPref_use_themes() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return cachedPref_use_themes; }


    protected void getColorPrimary() {
        mPrimaryColor = mPref.getInt("theme_PrimaryColor",0);

    }

    public int getCached_ColorPrimary() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return mPrimaryColor;
    }

    protected void getColorPrimaryDark() {
        mPrimaryDarkColor = mPref.getInt("theme_PrimaryColorDark",0);

    }

    public int getCached_ColorPrimaryDark() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return mPrimaryDarkColor;
    }
    protected void getColorAccent() {
        mAccentColor = mPref.getInt("theme_AccentColor",0);

    }

    public int getCached_ColorAccent() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return mAccentColor;
    }





}
