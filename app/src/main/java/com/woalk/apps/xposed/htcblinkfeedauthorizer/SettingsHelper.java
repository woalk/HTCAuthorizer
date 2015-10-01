package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Color;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = PACKAGE_NAME + "_preferences";

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes, cachedPref_theme_systemui;
    private int mPrimaryColor, mPrimaryDarkColor, mAccentColor;
    private String mRomType;



    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);
        loadCachePrefs();
    }

    public void loadCachePrefs() {
        mPref.reload();
        getPref_use_themes();
        getColorPrimary();
        getColorPrimaryDark();
        getColorAccent();
        getPref_romtype();
        getPref_theme_systemui();

    }

    protected void getPref_use_themes() {
        cachedPref_use_themes = mPref.getBoolean("use_themes", false);

    }

    protected void getPref_romtype() {
        mRomType = mPref.getString("romtype", "");

    }

    protected void getColorPrimary() {
        mPrimaryColor = mPref.getInt("theme_PrimaryColor", 0);

    }

    protected void getColorPrimaryDark() {
        mPrimaryDarkColor = mPref.getInt("theme_PrimaryColorDark", 0);

    }

    protected void getColorAccent() {
        mAccentColor = mPref.getInt("theme_AccentColor", 0);

    }

    protected void getPref_theme_systemui() {
        cachedPref_theme_systemui = mPref.getBoolean("systemui_use_launcher_theme", false);

    }

    public boolean getCachedPref_use_themes() {
        return cachedPref_use_themes;
    }

    public String getCachedPref_romtype() {
        return mRomType;
    }

    public Boolean getCachedPref_theme_systemui() {
        return cachedPref_theme_systemui;
    }

    public int getCached_ColorPrimary() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return Color.rgb(Color.red(mPrimaryColor), Color.green(mPrimaryColor), Color.blue(mPrimaryColor));
    }

    public int getCached_ColorPrimaryDark() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return Color.rgb(Color.red(mPrimaryDarkColor), Color.green(mPrimaryDarkColor), Color.blue(mPrimaryDarkColor));
    }

    public int getCached_ColorAccent() {
        Logger.d("Settingshelper: I've been invoked, returning " + cachedPref_use_themes + " for theme boolean.");
        return Color.rgb(Color.red(mAccentColor), Color.green(mAccentColor), Color.blue(mAccentColor));
    }


}
