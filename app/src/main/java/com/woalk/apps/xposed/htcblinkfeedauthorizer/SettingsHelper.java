package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Color;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "main";
    protected static final String PREFERENCE_THEME = "sensify_theme";

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes;
    private int theme_color1;
    private int theme_color2;
    private int theme_color3;
    private int theme_color4;
    private static final String shkey = "SettingsHelper: ";

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
        theme_color1 = mPref.getInt("systemui_color1", 0);
        theme_color2 = mPref.getInt("systemui_color2", 0);
        theme_color3 = mPref.getInt("systemui_color3", 0);
        theme_color4 = mPref.getInt("systemui_color4", 0);
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

    public boolean getPref_force_rotate() {
        return mPref.getBoolean("force_rotate", false);
    }

    protected int getThemeColor(int index) {
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

    public boolean getCachedPref_systemui_use_launcher_theme() {
        return getPref_systemui_use_launcher_theme();
    }

    protected boolean getPref_systemui_use_launcher_theme() {
        return mPref.getBoolean("systemui_use_launcher_theme", false);
    }

    public int getPrimaryColor() {
        return getThemeColor(1);
    }
    //do these when the themes are set by the hook instead, save them for colorpicker.
    public int getPrimaryDarkColor() {
        Integer p = getThemeColor(1);
        Integer s = getThemeColor(2);
        if (p.intValue() == s.intValue()) {
            Logger.i(shkey + "Secondary theme color not unique, mixing.");
            return Common.enlightColor(getThemeColor(2), 0.6f);
        } else {
            Logger.i(shkey + "Secondary theme color is unique, returning.");
            return s;
        }
    }

    public int getAccentColor() {
        Integer p = getThemeColor(1);
        Integer a = getThemeColor(3);
        if (p.intValue() == a.intValue()) {
            Logger.i(shkey + "Primary accent color not unique, mixing.");
            return Common.enlightColor(getThemeColor(1), 1.5f);
        } else {
            Logger.i(shkey + "Primary accent color is unique, returning.");
            return getThemeColor(3);
        }
    }

    public int getSecondaryAccentColor() {
        Integer p = getThemeColor(1);
        Integer a = getThemeColor(4);
        if (p.intValue() == a.intValue()) {
            Logger.i(shkey + "Secondary accent color not unique, mixing.");
            return Common.enlightColor(getThemeColor(1), .2f);
        } else {
            Logger.i(shkey + "Secondary accent color is unique, returning.");
            return getThemeColor(4);
        }
    }

    protected int getPref_systemui_color1() {
        int color = mPref.getInt("systemui_color1", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color2() {
        int color = getPrimaryDarkColor();
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color3() {
        int color = getAccentColor();
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color4() {
        int color = getSecondaryAccentColor();
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    public int getCachedPref_systemui_color1() {
        return getPref_systemui_color1();
    }

    public int getCachedPref_systemui_color2() {
        return getPref_systemui_color2();
    }

    public int getCachedPref_systemui_color3() {
        return getPref_systemui_color3();
    }

    public int getCachedPref_systemui_color4() {
        return getPref_systemui_color4();
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
                + "primary=" + Logger.getLogColorString(getPrimaryColor()) + ";"
                + "primaryD=" + Logger.getLogColorString(getPrimaryDarkColor()) + ";"
                + "accent=" + Logger.getLogColorString(getAccentColor()) + ";"
                + "Lprimary=" + Logger.getLogColorString(getPrimaryColor()) + ";"
                + "LprimaryD=" + Logger.getLogColorString(getPrimaryDarkColor()) + ";"
                + "Laccent=" + Logger.getLogColorString(getAccentColor()) + ";"
                + "useL=" + getCachedPref_systemui_use_launcher_theme() + ";"
                + "systemUI1=" + Logger.getLogColorString(getCachedPref_systemui_color1()) + ";"
                + "systemUI2=" + Logger.getLogColorString(getCachedPref_systemui_color2());
    }


}
