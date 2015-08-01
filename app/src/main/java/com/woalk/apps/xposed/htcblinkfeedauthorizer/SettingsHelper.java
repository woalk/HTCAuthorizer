package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "main";
    protected static final String PREFERENCE_THEME = "sensify_theme";

    public static final int DEFAULT_THEME_COLOR = 0xff0e5e8c;
    public static final int PLACEHOLDER_THEME_COLOR = 0xfe0189cd;

    private final XSharedPreferences mPref;

    private boolean cachedPref_use_themes;
    private int theme_color1;
    private int theme_color2;
    private int theme_color3;
    private int theme_color4;
    private int systemui_color1;
    private int systemui_color2;
    private boolean use_launcher_theme;
    private boolean systemui_use_launcher_theme;

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
                PREFERENCE_THEME);
        theme_color1 = themePref.getInt("full_theme_colo1", 0);
        theme_color2 = themePref.getInt("full_theme_colo2", 0);
        theme_color3 = themePref.getInt("full_theme_colo3", 0);
        theme_color4 = themePref.getInt("full_theme_colo4", 0);

        systemui_color1 = getPref_systemui_color1();
        systemui_color2 = getPref_systemui_color2();

        use_launcher_theme = getPref_use_launcher_theme();
        systemui_use_launcher_theme = getPref_systemui_use_launcher_theme();
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

    public boolean getCachedPref_use_launcher_theme() {
        return use_launcher_theme;
    }

    protected boolean getPref_use_launcher_theme() {
        return mPref.getBoolean("use_launcher_theme", false);
    }

    public boolean getCachedPref_systemui_use_launcher_theme() {
        return systemui_use_launcher_theme;
    }

    protected boolean getPref_systemui_use_launcher_theme() {
        return mPref.getBoolean("systemui_use_launcher_theme", false);
    }

    public int getPrimaryColor() {
        return getPrimaryColor(getCachedPref_use_launcher_theme());
    }

    public int getPrimaryColor(boolean launcher) {
        int c = getThemeColor(launcher ? 1 : 3);
        return c == 0 ? DEFAULT_THEME_COLOR : c;
    }

    public int getPrimaryDarkColor() {
        return Common.enlightColor(getPrimaryColor(), 0.6f);
    }

    public int getPrimaryDarkColor(boolean launcher) {
        return Common.enlightColor(getPrimaryColor(launcher), 0.6f);
    }

    public int getAccentColor() {
        return getAccentColor(getCachedPref_use_launcher_theme());
    }

    public int getAccentColor(boolean launcher) {
        int c = launcher ? Common.enlightColor(getThemeColor(1), 1.25f)
                : getThemeColor(2);
        return c == 0 ? Common.enlightColor(DEFAULT_THEME_COLOR, 1.25f) : c;
    }

    public int getCachedPref_systemui_color1() {
        int color = systemui_color1;
        if (color == PLACEHOLDER_THEME_COLOR) {
            color = getPrimaryColor(getCachedPref_systemui_use_launcher_theme());
        }
        return color;
    }

    protected int getPref_systemui_color1() {
        return mPref.getInt("systemui_color1", 0);
    }

    public int getCachedPref_systemui_color2() {
        int color = systemui_color2;
        if (color == PLACEHOLDER_THEME_COLOR) {
            color = getPrimaryDarkColor(getCachedPref_systemui_use_launcher_theme());
        }
        return color;
    }

    protected int getPref_systemui_color2() {
        return mPref.getInt("systemui_color2", 0);
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
                + "primary=" + Logger.getLogColorString(getPrimaryColor(false)) + ";"
                + "primaryD=" + Logger.getLogColorString(getPrimaryDarkColor(false)) + ";"
                + "accent=" + Logger.getLogColorString(getAccentColor(false)) + ";"
                + "Lprimary=" + Logger.getLogColorString(getPrimaryColor(true)) + ";"
                + "LprimaryD=" + Logger.getLogColorString(getPrimaryDarkColor(true)) + ";"
                + "Laccent=" + Logger.getLogColorString(getAccentColor(true)) + ";"
                + "useL=" + getCachedPref_use_launcher_theme() + ";"
                + "systemUI1=" + Logger.getLogColorString(getCachedPref_systemui_color1()) + ";"
                + "systemUI2=" + Logger.getLogColorString(getCachedPref_systemui_color2());
    }
}
