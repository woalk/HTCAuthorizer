package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Color;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = PACKAGE_NAME + "_preferences";

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes, cachedPref_theme_systemui,cachedPref_useUSB,cachedPref_useExternal,cachedPref_rotateLauncher;
    private int mPrimaryColor, mPrimaryDarkColor, mAccentColor;
    private String mRomType, cachedPref_pathUSB,cachedPref_pathExternal;



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
        getPref_useExternal();
        getPref_useUSB();
        getPref_rotateLauncher();
        getPref_pathExternal();
        getPref_pathUSB();



    }

    protected void getPref_pathUSB() {
        cachedPref_pathUSB = mPref.getString("usb_dir", "");

    }

    protected void getPref_pathExternal() {
        cachedPref_pathExternal = mPref.getString("ext_dir", "");

    }

    protected void getPref_useUSB() {
        cachedPref_useUSB = mPref.getBoolean("has_usb", false);

    }

    protected void getPref_useExternal() {
        cachedPref_useExternal = mPref.getBoolean("has_external", false);

    }

    protected void getPref_rotateLauncher() {
        cachedPref_rotateLauncher = mPref.getBoolean("force_rotate", false);

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

    public boolean getCachedPref_useExternal() {
        return cachedPref_useExternal;
    }

    public Boolean getCachedPref_useUSB() {
        return cachedPref_useUSB;
    }

    public Boolean getCachedPref_rotateLauncher() {
        return cachedPref_rotateLauncher;
    }

    public String getCachedPref_pathUSB() {
        return cachedPref_pathUSB;
    }

    public String getCachedPref_pathExternal() {
        return cachedPref_pathExternal;
    }


    public boolean getCachedPref_use_themes() {
        return cachedPref_use_themes;
    }

    public String getCachedPref_romType() {
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
