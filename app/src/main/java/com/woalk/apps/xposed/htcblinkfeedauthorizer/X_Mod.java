package com.woalk.apps.xposed.htcblinkfeedauthorizer;


import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.res.Resources;
import android.content.res.XResources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * The class to be loaded by Xposed.
 */
public class X_Mod
        implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    private static final String ACTIVITY_THREAD_CLASS = "android.app.ActivityThread";
    private static final String ACTIVITY_THREAD_CURRENTACTHREAD = "currentActivityThread";
    private static final String ACTIVITY_THREAD_GETSYSCTX = "getSystemContext";

    public static final String PKG_HTC_LAUNCHER = "com.htc.launcher";
    public static final String PKG_HTC_LIB0 = "com.htc.lib0";
    public static final String PKG_HTC_SOCIALNETWORK_UI = "com.htc.socialnetwork.common.utils.ui";

    public static final String CLASS_BF_HELPER = PKG_HTC_LAUNCHER + ".util.HspUpdateHelper";
    public static final String CLASS_BF_SETTINGUTIL = PKG_HTC_LAUNCHER + ".util.SettingUtil";
    public static final String CLASS_BF_LIB2 = "com.htc.lib2.Hms";
    public static final String CLASS_BF_UDACT = PKG_HTC_SOCIALNETWORK_UI + ".HMSUpdateActivity";
    public static final String CLASS_BF_PROFILEBRIEF = "com.htc.themepicker.model.ProfileBrief";
    public static final String CLASS_BF_MIXINGTHEMECOLOR = "com.htc.themepicker.util" +
            ".MixingThemeColorUtil";
    public static final String CLASS_BF_CURRENTTHEMEUTIL = "com.htc.themepicker.util.CurrentThemeUtil";
    public static final String CLASS_BF_THEME = "com.htc.themepicker.model.Theme";
    public static final String CLASS_BF_THEMECROP = "com.htc.themepicker.thememaker.WallpaperImageHandler";
    public static final String STRING_REBOOT = "Theme Applied, please reboot.";

    public static final String PKG_HTC_CAMERA = "com.htc.camera";
    public static final String CLASS_HTC_LIB3 = "com.htc.lib3.android.os.HtcEnvironment";
    public static final String CLASS_CAMERA_ZOECAPTUREMODE = "com.htc.camera.zoe.ZoeCaptureMode";
    public static final String CLASS_CAMERA_CAMERACONTROLLER = "com.htc.camera.CameraController";
    public static final String CLASS_CAMERA_FEATUREFILE = "com.htc.camera.CameraFeatureFile";
    public static final String CLASS_CAMERA_DISPLAYDEVICE = "com.htc.camera.DisplayDevice";

    public static final String PKG_HTC_GALLERY = "com.htc.album";

    public static final String PKG_HTC_FB = "com.htc.sense.socialnetwork.facebook";
    public static final String CLASS_FB_BASE_ACTIVITY2 = "com.htc.socialnetwork.facebook" +
            ".FacebookBaseActivity";
    public static final String CLASS_FB_UPDATE = "com.htc.socialnetwork.facebook.HMSUpdateActivity";

    public static final String PKG_HTC_GPLUS_APP = "com.htc.sense.socialnetwork.googleplus";
    public static final String PKG_HTC_GPLUS = "com.htc.socialnetwork.googleplus";
    public static final String CLASS_GPLUS_ACTIVITY = PKG_HTC_GPLUS + ".GooglePlusActivity";
    public static final String CLASS_GPLUS_DEEPLINK_ACTIVITY = PKG_HTC_GPLUS +
            ".DeeplinkRedirectActivity";

    public static final String PKG_HTC_INSTAGRAM = "com.htc.sense.socialnetwork.instagram";
    public static final String PKG_HTC_INSTAGRAM_COMM = PKG_HTC_INSTAGRAM + ".common";
    public static final String PKG_HTC_LIB2 = "com.htc.lib2";
    public static final String CLASS_HTC_BBA = "com.htc.b.b.a";

    public static final String PKG_HTC_LINKEDIN = "com.htc.sense.linkedin";
    public static final String PKG_HTC_LINKEDIN_COMM = PKG_HTC_LINKEDIN + ".common";
    public static final String CLASS_LINKEDIN_ACTIVITY = PKG_HTC_LINKEDIN_COMM +
            ".LinkedInActivity";
    public static final String CLASS_LINKEDIN_LIB2_A = PKG_HTC_LIB2 + ".a";

    public static final String PKG_HTC_TWITTER = "com.htc.sense.socialnetwork.twitter";
    public static final String PKG_HTC_TWITTER2 = "com.htc.htctwitter";
    public static final String CLASS_TWITTER_ACTIVITY = PKG_HTC_TWITTER2 +
            ".TwitterActivity";
    public static final String CLASS_TWITTER_DEEPLINK_ACTIVITY = PKG_HTC_TWITTER2 +
            ".DeeplinkRedirectActivity";

    public static final String PKG_HTC_IME = "com.htc.sense.ime";
    public static final String CLASS_IME_ASDK = PKG_HTC_IME + ".NonAndroidSDK$HtcAdded";
    public static final String CLASS_IME_AAB = "com.htc.a.a";

    public static final String CLASS_HDK0UTIL = PKG_HTC_LIB0 + ".HDKLib0Util";
    public static final String CLASS_BASE_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI + ".BaseActivity";
    public static final String CLASS_COMMON_MF_MAIN_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI +
            ".CommonMfMainActivity";

    public static final String CLASS_INSTAGRAM_ACTIVITY = PKG_HTC_INSTAGRAM_COMM +
            ".InstagramActivity";
    public static final String CLASS_INSTAGRAM_LIB2_A = PKG_HTC_LIB2 + ".a";
    public static final String CLASS_INSTAGRAM_DBA = "com.htc.sphere.d.b.a";

    public static final String PKG_VENDING = "com.android.vending";
    public static final String PKG_FINSKY = "com.google.android.finsky";
    public static final String PKG_FINSKY_API = "com.google.android.finsky.api.model";
    public static final String CLASS_FINSKY_LIBRARY_UTILS = PKG_FINSKY + ".utils.LibraryUtils";
    public static final String CLASS_FINSKY_DOCUMENT = PKG_FINSKY_API + ".Document";
    public static final String CLASS_FINSKY_DFETOC = PKG_FINSKY_API + ".DfeToc";
    public static final String CLASS_FINSKY_LIBRARY = PKG_FINSKY + ".library.Library";

    public static final String PKG_SYSTEMUI = "com.android.systemui";
    public static final String PKG_SETTINGS = "com.android.settings";
    public static final String PKG_DIALER = "com.google.android.dialer";
    public static final String PKG_DIALER2 = "com.android.dialer";
    public static final String PKG_CONTACTS = "com.android.contacts";
    public static final String PKG_GOOGLECONTACTS = "com.google.android.contacts";
    public static final String PKG_LATINIME = "com.android.inputmethod.latin";
    public static final String PKG_LATINIMEGOOGLE = "com.google.android.inputmethod.latin";
    public static final String CLASS_SETTINGS_DASHBOARD_SUMMARY = PKG_SETTINGS +
            ".dashboard.DashboardSummary";
    public static final String CLASS_SETTINGS_DASHBOARD_TILE = PKG_SETTINGS +
            ".dashboard.DashboardTile";
    public static final String CLASS_SETTINGS_SEARCH_RESULTS_SUMMARY = PKG_SETTINGS +
            ".dashboard.SearchResultsSummary";
    public static final String CLASS_SETTINGS_SEARCH_RESULTS_ADAPTER =
            CLASS_SETTINGS_SEARCH_RESULTS_SUMMARY + "$SearchResultsAdapter";
    public static final String CLASS_SETTINGS_CHARTNETWORK = PKG_SETTINGS +
            ".widget.ChartNetworkSeriesView";
    public static final String CLASS_PACKAGEMANAGER = "android.app.ApplicationPackageManager";
    public static final String PKG_HTC_FEATURE = "com.htc.software";
    public static final String[] HTC_FEATURES = new String[]{
            PKG_HTC_FEATURE + ".HTC",
            PKG_HTC_FEATURE + ".Sense7.0",
            PKG_HTC_FEATURE + ".M8UL",
            PKG_HTC_FEATURE + ".M8WL",
            PKG_HTC_FEATURE + ".IHSense",
            PKG_HTC_FEATURE + ".hdk",
            PKG_HTC_FEATURE + ".hdk2",
            PKG_HTC_FEATURE + ".hdk3"
    };
    private static boolean themesEnabled = false;
    private static boolean themeSystemUI = false;
    private static boolean useUSB = false;
    private static boolean useExternal = false;
    private static boolean rotateLauncher = false;
    private static String pathUSB;
    private static String romType;
    private static String pathExternal;
	private final SettingsHelper mSettings;
    private static int colorPrimary, colorPrimaryDark, colorAccent, commsPrimary, commsDark, commsAccent, infoPrimary, infoDark, infoAccent, entPrimary, entDark, entAccent;
    private static int cachedPrimary, cachedPrimaryDark, cachedAccent;

    private static final class Config {


        // Give us some sane defaults, just in case


        private static void reload(Context ctx) {
            try {
                Uri ALL_PREFS_URI = Uri.parse("content://" + SettingsProvider.AUTHORITY + "/all");
                ContentResolver contentResolver = ctx.getContentResolver();
                if (!(contentResolver == null)) {
                    Cursor prefs = contentResolver.query(ALL_PREFS_URI, null, null, null, null);
                    if (prefs == null) {
                        Logger.d("X_Mod: Failed to retrieve settings!");
                        return;
                    }
                    while (prefs.moveToNext()) {
                        int tempColor = 0;
                        switch (prefs.getString(SettingsProvider.QUERY_ALL_KEY)) {
                            case "use_themes":
                                themesEnabled = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE) == SettingsProvider.TRUE;
                                Logger.d("X_Mod: Variable read for theme enabled of " + themesEnabled);
                                continue;
                            case "systemui_use_launcher_theme":
                                themeSystemUI = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE) == SettingsProvider.TRUE;
                                Logger.d("X_Mod: Variable read for theme sysui enabled of " + themesEnabled);
                                continue;
                            case "theme_PrimaryColor":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                colorPrimary = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for primary color of " + colorPrimary);
                                continue;
                            case "theme_PrimaryDarkColor":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                colorPrimaryDark = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for primarydark color of " + colorPrimaryDark);
                                continue;
                            case "theme_AccentColor":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                colorAccent = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for primarydark color of " + colorPrimaryDark);
                                continue;
                            case "theme_Comms_Primary":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                commsPrimary = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Comms_Light":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                commsAccent = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Comms_Dark":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                commsDark = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Info_Primary":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                infoPrimary = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Info_Light":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                infoAccent = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Info_Dark":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                infoDark = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Entertainment_Primary":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                entPrimary = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Entertainment_Light":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                entAccent = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "theme_Entertainment_Dark":
                                tempColor = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE);
                                entDark = Color.rgb(Color.red(tempColor), Color.green(tempColor),
                                        Color.blue(tempColor));
                                Logger.d("X_Mod: Variable read for accent color of " + colorAccent);
                                continue;
                            case "has_ext":
                                useExternal = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE) == SettingsProvider.TRUE;
                                continue;
                            case "force_rotate":
                                rotateLauncher = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE) == SettingsProvider.TRUE;
                                Logger.d("X_Mod: Variable read for rotation of " + rotateLauncher);
                                continue;
                            case "ext_dir":
                                pathExternal = prefs.getString(SettingsProvider.QUERY_ALL_VALUE);
                                Logger.d("X_Mod: Variable read for external path of " + pathExternal);
                                continue;
                            case "usb_dir":
                                pathUSB = prefs.getString(SettingsProvider.QUERY_ALL_VALUE);
								Logger.d("X_Mod: Variable read for USB path of " + pathUSB);
                                continue;
                            case "romtype":
                                romType = prefs.getString(SettingsProvider.QUERY_ALL_VALUE);
                                Logger.v("X_Mod: Romtype identified as " + romType);
                                continue;
                            case "has_usb":
                                useUSB = prefs.getInt(SettingsProvider.QUERY_ALL_VALUE) == SettingsProvider.TRUE;


                        }
                    }
                    prefs.close();
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                Logger.e("X_Mod: NPE.  Probably settingsProvider isn't ready yet" + e);
            }
        }
    }

    public X_Mod() {
        Logger.logStart();
        mSettings = new SettingsHelper();
        cachedAccent = mSettings.getCached_ColorAccent();
        cachedPrimary = mSettings.getCached_ColorPrimary();
        cachedPrimaryDark = mSettings.getCached_ColorPrimaryDark();
		
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass(ACTIVITY_THREAD_CLASS, null), ACTIVITY_THREAD_CURRENTACTHREAD);
        final Context systemCtx = (Context) XposedHelpers.callMethod(activityThread, ACTIVITY_THREAD_GETSYSCTX);

        Config.reload(systemCtx);

        // First section contains common checks found in all HTC Apps
        // Need to see if OR statements are best, or if we can just check for com.htc.* apps
        if (lpparam.packageName.equals(PKG_HTC_LAUNCHER)) {

            Logger.v("Load hooks for Sense Home...");

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                    Context.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_SETTINGUTIL, lpparam.classLoader,
                    "isHtcDevice", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isHTCDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_LIB2, lpparam.classLoader, "isHtcDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BASE_ACTIVITY, lpparam.classLoader,
                    "checkCompatibility", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_COMMON_MF_MAIN_ACTIVITY, lpparam.classLoader,
                    "checkCompatibility", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_UDACT, lpparam.classLoader, "onCreate",
                    Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            ((Activity) param.thisObject).getIntent().setAction("ANY_ACTION");
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_PROFILEBRIEF, lpparam.classLoader,
                    "isHtc", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_PROFILEBRIEF, lpparam.classLoader, "isHtc",
                    CLASS_BF_PROFILEBRIEF, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                            Logger.logHookAfter(param);
                        }
                    });
            XposedHelpers.findAndHookMethod(CLASS_BF_THEMECROP, lpparam.classLoader, "startCropForResult", Activity.class, Uri.class, Point.class, Point.class, boolean.class, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Logger.d("X_Mod: Hooking theme crop activity.");
                    param.args[4] = false;
                    Logger.logHookAfter(param);
                }
            });


            // Theme permissions hook
            XposedHelpers.findAndHookMethod(CLASS_BF_MIXINGTHEMECOLOR, lpparam.classLoader,
                    "updateFullThemecolor", Context.class, CLASS_BF_THEME, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Logger.logHook(param);
                            Logger.v("X_Mod: HTC theme Hooked");
                            Context context = AndroidAppHelper.currentApplication();
                            Class getFullColorCodesClass = XposedHelpers.findClass("com.htc.themepicker.util.CurrentThemeUtil", lpparam.classLoader);
                            int[] result = (int[]) XposedHelpers.callStaticMethod(getFullColorCodesClass, "getFullColorCodes", context);

                            Intent intent = new Intent();
                            intent.setAction("com.woalk.HTCAuthorizer.UPDATE_XML");
                            intent.putExtra("full_Array", result);
                            context.sendBroadcast(intent);


                        }
                    });

            try {
                if (rotateLauncher) {
                    XposedHelpers.findAndHookMethod(Activity.class, "setRequestedOrientation",
                            int.class, new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws
                                        Throwable {
                                    // ORIENTATION_USER = 2, ORIENTATION_SENSOR = 4
                                    param.args[0] = 2;
                                }
                            });
                }
            } catch (Throwable e) {
                Logger.w("Rotation hook not loaded.", e);
            }

            Logger.v("All hooks for Sense Home loaded.");

        } else if (lpparam.packageName.equals(PKG_HTC_FB)) {

            Logger.v("Load hooks for Facebook...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_FB_BASE_ACTIVITY2, lpparam.classLoader,
                        "e", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "b", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_FB_UPDATE, lpparam.classLoader,
                        "onCreate", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "e", true);
                                ((Activity) param.thisObject).getIntent().setAction("ANY_ACTION");
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for Facebook loaded.");

            } catch (Throwable e) {
                Logger.w("Facebook hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_HTC_GPLUS_APP)) {

            Logger.v("Load hooks for Google+...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_GPLUS_ACTIVITY, lpparam.classLoader,
                        "f", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_GPLUS_DEEPLINK_ACTIVITY, lpparam.classLoader,
                        "d", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for Google+ loaded.");

            } catch (Throwable e) {
                Logger.w("Google+ hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals("com.woalk.apps.xposed.htcblinkfeedauthorizer")) {

            Logger.v("Load hooks for Sensify...");

            try {
                XposedHelpers.findAndHookMethod("com.woalk.apps.xposed.htcblinkfeedauthorizer.MainActivity", lpparam.classLoader,
                        "mHook",int.class, int.class, int.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                Logger.d("X_Mod: Hook called to replace systemwide");
                                mSettings.loadCachePrefs();
                                replaceSystemWideThemes();
                                Logger.logHookAfter(param);
                            }
                        });


                Logger.v("All hooks for Sensify loaded.");

            } catch (Throwable e) {
                Logger.w("Sensify hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_HTC_INSTAGRAM)) {

            Logger.v("Load hooks for Instagram...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(7.0f);
                                Logger.logHookAfter(param);
                            }
                        });


                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_DBA, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_HTC_BBA, lpparam.classLoader, "a",
                        new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                return null;
                            }
                        });


                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_ACTIVITY, lpparam.classLoader, "d",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "b_", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for Instagram loaded.");

            } catch (Throwable e) {
                Logger.w("Instagram hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_HTC_LINKEDIN)) {

            Logger.v("Load hooks for LinkedIn...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_LIB2_A, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(7.0f);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_ACTIVITY, lpparam.classLoader, "g",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for LinkedIn loaded.");

            } catch (Throwable e) {
                Logger.w("LinkedIn hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_HTC_TWITTER)) {

            Logger.v("Load hooks for Twitter...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_TWITTER_ACTIVITY, lpparam.classLoader, "d",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_TWITTER_DEEPLINK_ACTIVITY,
                        lpparam.classLoader, "b", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for Twitter loaded.");

            } catch (Throwable e) {
                Logger.w("Twitter hooks could not be loaded.", e);
            }


        } else if (lpparam.packageName.equals(PKG_HTC_CAMERA)) {

            Logger.v("Load hooks for Camera...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_CAMERA_FEATUREFILE, lpparam.classLoader,
                        "setStringValue", String.class, String.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                if (param.args[0].equals("zoe-supported")) {
                                    param.args[1] = "true";
                                    Logger.logHookAfter(param);
                                }
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_CAMERA_ZOECAPTUREMODE, lpparam.classLoader,
                        "checkZoeSupportState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_CAMERA_CAMERACONTROLLER, lpparam.classLoader,
                        "isZoeSupported", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_CAMERA_DISPLAYDEVICE, lpparam.classLoader,
                        "isHtcDevice", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for Camera loaded.");

            } catch (Throwable e) {
                Logger.w("Camera hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_HTC_IME)) {

            Logger.v("Load hooks for HTC IME...");

            try {
                XposedHelpers.findAndHookMethod(CLASS_IME_ASDK, lpparam.classLoader,
                        "isHTCDevice", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_IME_ASDK, lpparam.classLoader,
                        "isODMevice", Context.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });


                XposedHelpers.findAndHookMethod(CLASS_IME_AAB, lpparam.classLoader,
                        "a", Context.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_IME_AAB, lpparam.classLoader,
                        "b", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_IME_AAB, lpparam.classLoader,
                        "b", Context.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_IME_AAB, lpparam.classLoader,
                        "c", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(7.0f);
                                Logger.logHookAfter(param);
                            }
                        });

                Logger.v("All hooks for HTC IME loaded.");

            } catch (Throwable e) {
                Logger.w("HTC IME hooks could not be loaded.", e);
            }

        } else if (lpparam.packageName.equals(PKG_SETTINGS)
                && themesEnabled) {

            Logger.v("Load hooks to tint Settings app's icons with the Theme loaded at boot...");
            XposedHelpers.findAndHookMethod(Preference.class, "setIcon", Drawable.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Logger.logHook(param);
                            try {
                                Drawable d = (Drawable) param.args[0];
                                d.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);
                                param.args[0] = d;
                            } catch (NullPointerException e) {
                                Logger.e("X_Mod - error hooking icons" + e);
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_SETTINGS_DASHBOARD_SUMMARY, lpparam.classLoader,
                    "updateTileView", Context.class, Resources.class, CLASS_SETTINGS_DASHBOARD_TILE,
                    ImageView.class, TextView.class, TextView.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ImageView iV = (ImageView) param.args[3];
                            Drawable d = iV.getDrawable();
                            BitmapDrawable b = new BitmapDrawable((Resources) param.args[1],
                                    Common.drawableToBitmap(d));
                            b.setTint(colorAccent);
                            iV.setImageDrawable(b);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_SETTINGS_SEARCH_RESULTS_ADAPTER,
                    lpparam.classLoader, "getView", int.class, View.class, ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ViewGroup v = (ViewGroup) param.getResult();
                            ImageView iV = Common.findFirstImageView(v);
                            if (iV == null) return;
                            Drawable d = iV.getDrawable();
                            d.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);
                            iV.setImageDrawable(d);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_SETTINGS_CHARTNETWORK, lpparam.classLoader,
                    "setChartColor", int.class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[1] = colorPrimary;
                            param.args[2] = colorPrimaryDark;
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod("com.android.settings.DreamSettings$DreamInfoAdapter",
                    lpparam.classLoader, "createDreamInfoRow", ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ViewGroup v = (ViewGroup) param.getResult();
                            ImageView iV = Common.findLastImageView(v);
                            if (iV == null) return;
                            Drawable d = iV.getDrawable();
                            d.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);
                            iV.setImageDrawable(d);
                            Logger.logHookAfter(param);
                        }
                    });

            Logger.v("All hooks to tint Settings app's icons loaded.");

        } else if (lpparam.packageName.equals(PKG_VENDING)) {

            Logger.v("Load Play Store hooks...");

            XposedHelpers.findAndHookMethod(CLASS_FINSKY_LIBRARY_UTILS,
                    lpparam.classLoader, "isAvailable", CLASS_FINSKY_DOCUMENT, CLASS_FINSKY_DFETOC,
                    CLASS_FINSKY_LIBRARY, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object doc = param.args[0];
                            String id = (String) XposedHelpers.callMethod(doc, "getDocId");
                            if (id.startsWith("com.htc.")) {
                                Logger.logHook(param);
                                param.setResult(true);
                                Logger.logHookAfter(param);
                            }
                        }
                    });

            Logger.v("All Play Store hooks loaded.");

        }

        if (lpparam.packageName.equals(PKG_HTC_GALLERY)
                || lpparam.packageName.equals(PKG_HTC_CAMERA)) {
            Logger.v("X_Mod: Loading storage hooks for package %s.", lpparam.packageName);
            int hooks = 0;

            // Following: HTC-specific methods that resolve different storage types
            // try-catch for each necessary because not every HTC app uses all of them

            // Phone storage (non-removable) => internal sdcard
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasPhoneStorage", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                // Checks PRIMARY "external" storage.
                                // Android recognizes INTERNAL sdcard as primary external storage.
                                // If no internal sdcard, primary storage would be real sdcard.
                                param.setResult(!Environment.isExternalStorageRemovable());
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("hasPhoneStorage() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getPhoneStorageDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(!Environment.isExternalStorageRemovable() ?
                                        Environment.getExternalStorageDirectory()
                                        : null);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getPhoneStorageDirectory() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getPhoneStorageState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(Environment.isExternalStorageRemovable() ?
                                        Environment.getExternalStorageState()
                                        : Environment.MEDIA_UNKNOWN);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getPhoneStorageState() hook", e);
            }

            // Removable storage => external sdcard
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasRemovableStorageSlot", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useExternal);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("hasRemovableStorageSlot() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getRemovableStorageDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useExternal ?
                                        new File(pathExternal) : null);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getRemovableStorageDirectory() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getRemovableStorageState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useExternal ?
                                        Environment.getExternalStorageState(
                                                new File(pathExternal))
                                        : Environment.MEDIA_UNKNOWN);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getRemovableStorageState() hook", e);
            }

            // USB storage (OTG)
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasUsbDeviceSlot", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useUSB);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("hasUsbDeviceSlot() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getUsbDeviceDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useUSB ?
                                        new File(pathUSB) : null);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getUsbDeviceDirectory() hook", e);
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getUsbDeviceState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                Logger.logHook(param);
                                param.setResult(useUSB ?
                                        Environment.getExternalStorageState(
                                                new File(pathUSB))
                                        : Environment.MEDIA_UNKNOWN);
                                Logger.logHookAfter(param);
                            }
                        });
                hooks++;
            } catch (Throwable e) {
                Logger.i("A storage hook failed for package %s.", lpparam.packageName);
                Logger.i("getUsbDeviceState() hook", e);
            }

            Logger.v("All storage hooks processed for package %s, loaded %d/9.",
                    lpparam.packageName, hooks);
        }
    }

    @Override
    public void handleInitPackageResources(final XC_InitPackageResources.InitPackageResourcesParam
                                                   resparam) throws Throwable {
        if (themesEnabled) {

            if (resparam.packageName.equals(PKG_SYSTEMUI) && themeSystemUI && romType.equals("Google")) {
                Logger.v("X_Mod: ROM build identified as " + romType);
                Logger.v("X_Mod: Replacing Theme resources for Google SystemUI.");

                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_primary_color",
                        colorPrimary);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "screen_pinning_request_bg",
                        colorAccent);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "keyguard_avatar_frame_pressed_color",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_secondary_color",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_accent_color",
                        colorAccent);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "qs_detail_progress_track",
                        colorAccent);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "notification_material_background_media_default_color",
                        colorPrimary);

                Logger.v("X_Mod: Replaced Theme resources for Google SystemUI.");

            } else if (resparam.packageName.equals(PKG_SYSTEMUI) && romType.equals("Sense")) {
                Logger.v("X_Mod: ROM build identified as " + romType);
                if (themeSystemUI) {
                    Logger.v("X_Mod: Replacing Theme resources for Sense SystemUI.");

                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_primary_color",
                            colorPrimary);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "category_color",
                            colorAccent);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "keyguard_avatar_frame_pressed_color",
                            colorAccent);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_category_color",
                            colorPrimary);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_dark_category_color", colorPrimaryDark);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_multiply_color", colorPrimary);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_overlay_color", colorPrimary);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_dark_progress_fill_center_color", colorPrimary);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_dark_progress_fill_end_color", colorPrimaryDark);

                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "CategoryTwo_text_selection_color",
                            colorPrimaryDark);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_accent_color",
                            colorAccent);
                    resparam.res.setReplacement(PKG_SYSTEMUI, "color", "qs_detail_progress_track",
                            colorAccent);

                    Logger.v("X_Mod: Replaced Theme resources for Sense SystemUI.");
                }
            } else if (resparam.packageName.equals(PKG_SETTINGS)) {
                Logger.v("Replacing Theme resources for Settings app.");

                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary",
                        colorPrimary);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary_dark",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_accent",
                        colorAccent);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "switchbar_background_color",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "switch_accent_color",
                        colorAccent);
                resparam.res.hookLayout(PKG_SETTINGS, "layout", "preference_bluetooth", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                        ImageView settings = (ImageView) liparam.view.findViewById(
                                liparam.res.getIdentifier("deviceDetails", "id", PKG_SETTINGS));
                        settings.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);

                    }

                });
                resparam.res.hookLayout(PKG_SETTINGS, "layout", "preference_bluetooth_profile", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                        ImageView settings = (ImageView) liparam.view.findViewById(
                                liparam.res.getIdentifier("profileExpand", "id", PKG_SETTINGS));
                        settings.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);

                    }

                });
                resparam.res.hookLayout(PKG_SETTINGS, "layout", "wifi_display_preference", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                        ImageView settings = (ImageView) liparam.view.findViewById(
                                liparam.res.getIdentifier("deviceDetails", "id", PKG_SETTINGS));
                        settings.setColorFilter(colorAccent, PorterDuff.Mode.MULTIPLY);

                    }

                });


                Logger.v("Replaced Theme resources for Settings app.");
            } else if (resparam.packageName.equals(PKG_DIALER)) {
                Logger.v("Replacing Theme resources for Dialer app.");

                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_highlighted_text_holo_light",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_highlighted_text_holo_dark",
                        commsDark);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_holo_blue_light",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_link_text_light",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialer_theme_color",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialer_theme_color_dark",
                        commsDark);
                resparam.res.setReplacement(PKG_DIALER2, "color", "setting_primary_color",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "setting_secondary_color",
                        commsDark);
                resparam.res.setReplacement(PKG_DIALER2, "color", "button_selected_color",
                        commsDark);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialtacts_theme_color",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "glowpad_call_widget_normal_tint",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "incall_background_color",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "actionbar_background_color",
                        commsPrimary);
                resparam.res.setReplacement(PKG_DIALER2, "color", "actionbar_background_color_dark",
                        commsDark);
                resparam.res.setReplacement(PKG_DIALER2, "color", "call_log_voicemail_highlight_color",
                        commsAccent);
                resparam.res.setReplacement(PKG_DIALER2, "color", "contact_list_name_text_color",
                        commsAccent);
                resparam.res.setReplacement(PKG_DIALER2, "color", "call_log_extras_text_color",
                        commsAccent);
                resparam.res.setReplacement(PKG_DIALER2, "color", "voicemail_playback_seek_bar_already_played",
                        commsAccent);
                resparam.res.setReplacement(PKG_DIALER2, "color", "item_selected",
                        commsPrimary);
                resparam.res.hookLayout(PKG_DIALER2, "layout", "dialtacts_activity", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                        FrameLayout fab = (FrameLayout) liparam.view.findViewById(
                                liparam.res.getIdentifier("floating_action_button_container", "id", PKG_DIALER2));
                        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
                        sd.setIntrinsicHeight(10);
                        sd.setIntrinsicWidth(10);
                        sd.getPaint().setColor(commsPrimary);
                        fab.setBackground(sd);

                    }

                });
                resparam.res.hookLayout(PKG_DIALER2, "layout", "dialpad_fragment", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                        FrameLayout fab = (FrameLayout) liparam.view.findViewById(
                                liparam.res.getIdentifier("dialpad_floating_action_button_container", "id", PKG_DIALER2));
                        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
                        sd.setIntrinsicHeight(10);
                        sd.setIntrinsicWidth(10);
                        sd.getPaint().setColor(commsPrimary);
                        fab.setBackground(sd);

                    }

                });


                Logger.v("Replaced Theme resources for Dialer app.");
            } else if (resparam.packageName.equals(PKG_GOOGLECONTACTS)) {
                Logger.v("Replacing Theme resources for Contacts app.");

                resparam.res.setReplacement(PKG_CONTACTS, "color", "floating_action_button_icon_color",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialer_theme_color",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "wallet_holo_blue_light",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialer_theme_color_dark",
                        commsPrimary);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "primary_color",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "primary_color_dark",
                        commsPrimary);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "action_bar_background",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "actionbar_background_color",
                        commsDark);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "actionbar_background_color_dark",
                        commsPrimary);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialtacts_theme_color",
                        commsPrimary);

                resparam.res.hookLayout(PKG_CONTACTS, "layout", "floating_action_button", new XC_LayoutInflated() {
                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                        ImageButton fab = (ImageButton) liparam.view.findViewById(liparam.res.getIdentifier("floating_action_button", "id", PKG_CONTACTS));
                        fab.getBackground().setColorFilter(commsPrimary, PorterDuff.Mode.SRC_IN);
                    }
                });

                Logger.v("Replaced Theme resources for Contacts app.");

            } else if (resparam.packageName.equals(PKG_LATINIMEGOOGLE)) {
                Logger.v("Replacing Theme resources for Google Keyboard.");
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_background_lxx_dark",
                        colorPrimary);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_text_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_functional_text_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_text_inactive_color_lxx_light",
                        colorPrimary);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_hint_letter_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "language_on_spacebar_text_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "auto_correct_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "typed_word_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "suggested_word_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_background_pressed_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "suggested_word_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "highlight_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "gesture_trail_color_lxx_light",
                        colorAccent);
                resparam.res.setReplacement(PKG_LATINIME, "color", "sliding_key_input_preview_color_lxx_light",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "sliding_key_input_preview_color_lxx_dark",
                        colorPrimaryDark);
                resparam.res.setReplacement(PKG_LATINIME, "color", "gesture_trail_color_lxx_dark",
                        colorPrimaryDark);

                Logger.v("Replaced Theme resources for Contacts app.");

            } else if (resparam.packageName.equals(PKG_HTC_LAUNCHER)) {
                Logger.v("Replacing string resource for Sense Home.");

                resparam.res.setReplacement(PKG_HTC_LAUNCHER, "string", "msg_full_theme_applied",
                        STRING_REBOOT);

                Logger.v("Replaced string resource for Sense Home.");
            }
        } else {
            Logger.v("X_Mod: Themes disabled ");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("Sensify: initZygote Started");


        Logger.v("X_Mod: Loading hook to add HTC features to system feature list...");

        XposedHelpers.findAndHookMethod(CLASS_PACKAGEMANAGER, null, "getSystemAvailableFeatures",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        FeatureInfo[] sys = (FeatureInfo[]) param.getResult();
                        FeatureInfo[] fs = new FeatureInfo[sys.length + HTC_FEATURES.length];
                        System.arraycopy(sys, 0, fs, 0, sys.length);
                        for (int i = sys.length; i < sys.length + HTC_FEATURES.length; i++) {
                            fs[i] = new FeatureInfo();
                            fs[i].name = HTC_FEATURES[i - sys.length];
                        }
                        param.setResult(fs);
//                        Logger.logHookAfter(param);
                    }
                });

        Logger.v("System feature list hook loaded.");
        if (mSettings.getCachedPref_use_themes()) {
            replaceSystemWideThemes();
            Logger.v("X_Mod:Themes are enabled in module settings.");
            XposedBridge.log("Systemwide themes enabled");
        } else {
            Logger.v("X_Mod:Themes are turned off in module settings.");
            XposedBridge.log("Systemwide themes disabled");
        }
    }

    public static void replaceSystemWideThemes() {


        Logger.v("Replacing system-wide Theme resources.");

        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_900",
                cachedPrimary);
        Logger.v("X_Mod: trying to set a color for systemwide " + cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "user_icon_1",
                cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "highlighted_text_material_dark",
                cachedPrimaryDark);
        XResources.setSystemWideReplacement("android", "color", "highlighted_text_material_light",
                cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "primary_material_dark",
                cachedPrimaryDark);
        XResources.setSystemWideReplacement("android", "color", "primary_material_light",
                cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_950",
                cachedPrimaryDark);
        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_800",
                cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "primary_dark_material_dark",
                cachedPrimaryDark);
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_500",
                cachedPrimary);
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_200",
                cachedAccent);
        XResources.setSystemWideReplacement("android", "color", "accent_material_dark",
                cachedAccent);
        XResources.setSystemWideReplacement("android", "color", "accent_material_light",
                cachedAccent);
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_200",
                cachedPrimaryDark);


        Logger.v("Theme resources replaced.");
//        if (romType.equals("Sense")) {
//            Logger.d("X_Mod: Replacing Sense system resources.");
//            XResources.setSystemWideReplacement("android", "color", "text_selection_opacity_color", colorAccent);
//            XResources.setSystemWideReplacement("android", "color", "text_selection_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "light_category_color", colorAccent);
//            XResources.setSystemWideReplacement("android", "color", "category_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "dark_category_color", colorPrimaryDark);
//            XResources.setSystemWideReplacement("android", "color", "overlay_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "standard_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "active_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryOne_active_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_text_selection_opacity_color", colorAccent);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_text_selection_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_light_category_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_category_color", colorPrimaryDark);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_dark_category_color", colorPrimaryDark);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_multiply_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryTwo_overlay_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryThree_active_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_text_selection_opacity_color", colorAccent);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_text_selection_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_light_category_color", colorAccent);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_category_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_dark_category_color", colorPrimaryDark);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_overlay_color", colorPrimary);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_standard_color", colorPrimaryDark);
//            XResources.setSystemWideReplacement("android", "color", "CategoryFour_active_color", colorPrimary);
//        }

    }

}
