package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Color;

import java.io.IOException;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsHelper {
    protected static final String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
    protected static final String PREFERENCE_FILE = "main";
    protected static final String PREFERENCE_THEME = "sensify_theme";

    private final XSharedPreferences mPref;
    private final XMLHelper xw;

    private boolean cachedPref_use_themes;

    public Integer color1;
    public Integer color2;
    public Integer color3;
    public Integer color4;

    public SettingsHelper() {
        mPref = new XSharedPreferences(PACKAGE_NAME, PREFERENCE_FILE);

        loadCachePrefs();

        xw = new XMLHelper();
        updateFromXML();
    }


    public void loadCachePrefs() {
        cachedPref_use_themes = getPref_use_themes();
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


    public boolean getCachedPref_systemui_use_launcher_theme() {
        return getPref_systemui_use_launcher_theme();
    }

    protected boolean getPref_systemui_use_launcher_theme() {
        return mPref.getBoolean("systemui_use_launcher_theme", false);
    }


    protected int getPref_systemui_color1() {
        int color = color1;
        Logger.d("Settingshelper: color read is" + color1);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color2() {
        int color = mPref.getInt("systemui_color2", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color3() {
        int color = mPref.getInt("systemui_color3", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    protected int getPref_systemui_color4() {
        int color = mPref.getInt("systemui_color4", 0);
        return Color.rgb(Color.red(color), Color.green(color),
                Color.blue(color));
    }

    public int getCachedPref_systemui_color1() {
        Logger.d("Settingshelper: " + color1);
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

                + "useL=" + getCachedPref_systemui_use_launcher_theme() + ";"
                + "systemUI1=" + Logger.getLogColorString(getCachedPref_systemui_color1()) + ";"
                + "systemUI2=" + Logger.getLogColorString(getCachedPref_systemui_color2());
    }

    public void updateFromXML() {
        Logger.i("Digitalhigh: Starting Editor");

        try {
            color1 = xw.readFromXML(0);
            color2 = xw.readFromXML(1);
            color3 = xw.readFromXML(2);
            color4 = xw.readFromXML(3);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("Error reading from file" + e);
        }
        Logger.i("Digitalhigh: Colors set to " + color1 + " " + color2 + " " + color3 + " " + color4);


    }
}
