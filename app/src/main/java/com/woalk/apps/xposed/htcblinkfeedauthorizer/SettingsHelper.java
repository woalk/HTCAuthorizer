package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "main";

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes;
    private int theme_color1;
    private int theme_color2;
    private int theme_color3;
    private int theme_color4;
    private int systemui_color1;
    private int systemui_color2;

    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);
        loadCachePrefs();
        if (getCachedPref_use_themes()) {
            loadTheme();
        }
    }

    public void loadCachePrefs() {
        cachedPref_use_themes = getPref_use_themes();
    }

    public void loadTheme() {
        XSharedPreferences themePref = new XSharedPreferences(X_Mod.PKG_HTC_LAUNCHER,
                "mixing_theme_color_preference");
        theme_color1 = themePref.getInt("full_theme_colo1", 0);
        theme_color2 = themePref.getInt("full_theme_colo2", 0);
        theme_color3 = themePref.getInt("full_theme_colo3", 0);
        theme_color4 = themePref.getInt("full_theme_colo4", 0);

        systemui_color1 = getPref_systemui_color1();
        systemui_color2 = getPref_systemui_color2();
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

    public int getThemeColor(int index) {
        switch (index) {
            case 1:
                return theme_color1;
            case 2:
                return theme_color2;
            case 3:
                return theme_color3;
            case 4:
                return theme_color4;
            default:
                return 0;
        }
    }

    public int getCachedPref_systemui_color1() {
        int color = systemui_color1;
        if (color == 0xfe0189cd) {
            color = getThemeColor(1);
        }
        return color;
    }

    protected int getPref_systemui_color1() {
        return mPref.getInt("systemui_color1", 0);
    }

    public int getCachedPref_systemui_color2() {
        int color = systemui_color2;
        if (color == 0xfe0189cd) {
            color = Common.enlightColor(getThemeColor(1), 0.6f);
        }
        return color;
    }

    protected int getPref_systemui_color2() {
        return mPref.getInt("systemui_color2", 0);
    }
}
