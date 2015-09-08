package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Color;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "com.woalk.apps.xposed.htcblinkfeedauthorizer_preferences";
    protected static final String PREFERENCE_THEME = "sensify_theme";

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes, cachedPref_mix_themes;

    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);

        loadCachePrefs();

    }

    public void loadCachePrefs() {
        cachedPref_use_themes = getPref_use_themes();
        cachedPref_mix_themes = getPref_mix_themes();
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

    public boolean getCachedPref_use_themes() {
        return cachedPref_use_themes;
    }


    protected boolean getPref_use_themes() {
        return mPref.getBoolean("use_themes", false);
    }

    protected boolean getPref_mix_themes() {
        return mPref.getBoolean("use_themes", false);
    }

    public boolean getPref_force_rotate() {
        return mPref.getBoolean("force_rotate", false);
    }


    public boolean getCachedPref_systemui_use_launcher_theme() {
        return getPref_systemui_use_launcher_theme();
    }

    protected boolean getPref_systemui_use_launcher_theme() {
        return mPref.getBoolean("systemui_use_launcher_theme", false);
    }


    protected int getPref_theme_PrimaryColor() {
        int color = mPref.getInt("theme_PrimaryColor", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_theme_PrimaryDarkColor() {
        int color = mPref.getInt("theme_PrimaryDarkColor", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_theme_AccentColor() {
        int color = mPref.getInt("theme_AccentColor", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }


    public int getCachedPref_theme_PrimaryColor() {
        return getPref_theme_PrimaryColor();
    }

    public int getCachedPref_theme_PrimaryDarkColor() {
        return getPref_theme_PrimaryDarkColor();
    }

    public int getCachedPref_theme_AccentColor() {
        return getPref_theme_AccentColor();
    }



    @Override
    public String toString() {
        return super.toString() + "//"
                + "modPrefFile=" + mPref.getFile().toString() + "//" + "theme:" + theme_toString();
    }

    public String theme_toString() {
        if (!getCachedPref_use_themes()) {
            return "use=false";
        }
        return "use=true;"

                + "useL=" + getCachedPref_systemui_use_launcher_theme() + ";"
                + "systemUI1=" + Logger.getLogColorString(getCachedPref_theme_PrimaryColor()) + ";"
                + "systemUI2=" + Logger.getLogColorString(getCachedPref_theme_PrimaryDarkColor());
    }


}
