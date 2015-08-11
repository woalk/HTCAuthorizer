package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.res.Resources;
import android.content.res.XResources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * The class to be loaded by Xposed.
 */
public class X_Mod
        implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {
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
    public static final String CLASS_BF_THEME = "com.htc.themepicker.model.Theme";
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
    private final SettingsHelper mSettings;
    public XMLHelper xh;

    public X_Mod() {
        Logger.logStart();
        mSettings = new SettingsHelper();
        xh = new XMLHelper();


        Logger.v("Loaded preference instance %s", mSettings.toString());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
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

            // Theme permissions hook
            XposedHelpers.findAndHookMethod(CLASS_BF_MIXINGTHEMECOLOR, lpparam.classLoader,
                    "updateFullThemecolor", Context.class, CLASS_BF_THEME, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Logger.logHook(param);
                            Logger.v("Digitalhigh: HTC theme Hooked");
                            SharedPreferences theme_in = ((Context) param.args[0])
                                    .getSharedPreferences("mixing_theme_color_preference",
                                            Context.MODE_PRIVATE);
                            //noinspection deprecation
                            @SuppressLint("WorldReadableFiles")
                            SharedPreferences.Editor theme_out = ((Context) param.args[0])
                                    .getSharedPreferences(SettingsHelper.PREFERENCE_THEME,
                                            Context.MODE_WORLD_READABLE).edit();
                            for (Map.Entry<String, ?> x : theme_in.getAll().entrySet()) {
                                Logger.v("Digitalhigh: Reading HTC Theme " + x.getKey() + " " + x.getValue());
                                if (x.getValue() instanceof Integer) {
                                    Logger.v("Digitalhigh: Trying to pass HTC theme to writer " + x.getKey() + " " + x.getValue());
                                    if (x.getKey().contains("1")) {
                                        Logger.v("Digitalhigh: Found key containing 1");
                                        xh.WriteToXML("systemui_color1", (Integer) x.getValue());
                                    } else if (x.getKey().contains("2")) {
                                        Logger.v("Digitalhigh: Found key containing 1");
                                        xh.WriteToXML("systemui_color2", (Integer) x.getValue());
                                    } else if (x.getKey().contains("3")) {
                                        Logger.v("Digitalhigh: Found key containing 1");
                                        xh.WriteToXML("systemui_color3", (Integer) x.getValue());
                                    } else if (x.getKey().contains("4")) {
                                        Logger.v("Digitalhigh: Found key containing 1");
                                        xh.WriteToXML("systemui_color4", (Integer) x.getValue());
                                    }

                                } else {
                                    Logger.e("Digitalhigh: Error reading HTC Theme");
                                }
                            }
                            theme_out.apply();
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_MIXINGTHEMECOLOR, lpparam.classLoader,
                    "clearFullThemeColor", Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Logger.logHook(param);
                            //noinspection deprecation
                            @SuppressLint("WorldReadableFiles")
                            SharedPreferences theme = ((Context) param.args[0])
                                    .getSharedPreferences(SettingsHelper.PREFERENCE_THEME,
                                            Context.MODE_WORLD_READABLE);
                            SharedPreferences.Editor theme_edit = theme.edit();
                            for (String x : theme.getAll().keySet()) {
                                theme_edit.remove(x);
                            }
                            theme_edit.apply();
                        }
                    });

            try {

                if (new SettingsHelper().getPref_force_rotate()) {
                    Logger.v("Loading hook for BlinkFeed rotation.");
                    XposedHelpers.findAndHookMethod(Activity.class, "setRequestedOrientation",
                            int.class, new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws
                                        Throwable {
                                    // ORIENTATION_USER = 2, ORIENTATION_SENSOR = 4
                                    param.args[0] = 2;
                                }
                            });
                } else {
                    Logger.v("Rotation seems to be disabled");
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

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(true);
                                Logger.logHookAfter(param);
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
                && mSettings.getCachedPref_use_themes()) {

            Logger.v("Load hooks to tint Settings app's icons with the Theme loaded at boot...");
            Logger.logTheme(mSettings);
            final int color3 = mSettings.getCachedPref_systemui_color3();
            XposedHelpers.findAndHookMethod(Preference.class, "setIcon", Drawable.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Logger.logHook(param);
                            Drawable d = (Drawable) param.args[0];
                            BitmapDrawable b = new BitmapDrawable(((Preference) param.thisObject)
                                    .getContext().getResources(), Common.drawableToBitmap(d));
                            b.setTint(color3);
                            param.args[0] = b;
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
                            b.setTint(color3);
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
                            BitmapDrawable b = new BitmapDrawable(iV.getContext().getResources(),
                                    Common.drawableToBitmap(d));
                            b.setTint(color3);
                            iV.setImageDrawable(b);
                            Logger.logHookAfter(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_SETTINGS_CHARTNETWORK, lpparam.classLoader,
                    "setChartColor", int.class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[1] = mSettings.getAccentColor();
                            param.args[2] = Common.enlightColor(mSettings.getAccentColor(), 1.5f);
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
            Logger.v("Loading storage hooks for package %s.", lpparam.packageName);
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
                                param.setResult(mSettings.getPref_has_ext());
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
                                param.setResult(mSettings.getPref_has_ext() ?
                                        new File(mSettings.getPref_ext_path()) : null);
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
                                param.setResult(mSettings.getPref_has_ext() ?
                                        Environment.getExternalStorageState(
                                                new File(mSettings.getPref_ext_path()))
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
                                param.setResult(mSettings.getPref_has_usb());
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
                                param.setResult(mSettings.getPref_has_usb() ?
                                        new File(mSettings.getPref_usb_path()) : null);
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
                                param.setResult(mSettings.getPref_has_usb() ?
                                        Environment.getExternalStorageState(
                                                new File(mSettings.getPref_usb_path()))
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
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam
                                                   resparam) throws Throwable {
        int color1 = mSettings.getCachedPref_systemui_color1();
        int color2 = mSettings.getCachedPref_systemui_color2();
        int color3 = mSettings.getCachedPref_systemui_color3();
        int color4 = mSettings.getCachedPref_systemui_color4();

        if (mSettings.getCachedPref_use_themes()) {

            if (resparam.packageName.equals(PKG_SYSTEMUI) && mSettings.getPref_systemui_use_launcher_theme()) {
                Logger.v("Replacing Theme resources for SystemUI.");
                Logger.logTheme(mSettings);

                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_primary_color",
                        color1);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_secondary_color",
                        color2);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_accent_color",
                        color3);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "qs_detail_progress_track",
                        color4);
                resparam.res.setReplacement(PKG_SYSTEMUI, "color", "notification_material_background_media_default_color",
                        color2);

                Logger.v("Replaced Theme resources for SystemUI.");
            } else if (resparam.packageName.equals(PKG_SETTINGS)) {
                Logger.v("Replacing Theme resources for Settings app.");
                Logger.logTheme(mSettings);

                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary",
                        color1);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary_dark",
                        color2);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_accent",
                        color3);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "switchbar_background_color",
                        color2);
                resparam.res.setReplacement(PKG_SETTINGS, "color", "switch_accent_color",
                        color4);

                Logger.v("Replaced Theme resources for Settings app.");
            } else if (resparam.packageName.equals(PKG_DIALER)) {
                Logger.v("Replacing Theme resources for Dialer app.");
                Logger.logTheme(mSettings);

                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_highlighted_text_holo_light",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_highlighted_text_holo_dark",
                        color2);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_holo_blue_light",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "wallet_link_text_light",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialer_theme_color",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialer_theme_color_dark",
                        color2);
                resparam.res.setReplacement(PKG_DIALER2, "color", "dialtacts_theme_color",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "actionbar_background_color_dark",
                        color2);
                resparam.res.setReplacement(PKG_DIALER2, "color", "call_log_voicemail_highlight_color",
                        color4);
                resparam.res.setReplacement(PKG_DIALER2, "color", "call_log_extras_text_color",
                        color1);
                resparam.res.setReplacement(PKG_DIALER2, "color", "voicemail_playback_seek_bar_already_played",
                        color4);
                resparam.res.setReplacement(PKG_DIALER2, "color", "item_selected",
                        color1);

                Logger.v("Replaced Theme resources for Dialer app.");
            } else if (resparam.packageName.equals(PKG_GOOGLECONTACTS)) {
                Logger.v("Replacing Theme resources for Contacts app.");
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialer_theme_color",
                        color1);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "wallet_holo_blue_light",
                        color1);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialer_theme_color_dark",
                        color2);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "primary_color",
                        color1);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "primary_color_dark",
                        color2);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "action_bar_background",
                        color1);
                resparam.res.setReplacement(PKG_CONTACTS, "color", "dialtacts_theme_color",
                        color2);

                Logger.v("Replaced Theme resources for Contacts app.");

            } else if (resparam.packageName.equals(PKG_LATINIMEGOOGLE)) {
                Logger.v("Replacing Theme resources for Google Keyboard.");
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_background_lxx_dark",
                        color1);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_text_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_functional_text_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_text_inactive_color_lxx_light",
                        color1);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_hint_letter_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "language_on_spacebar_text_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "auto_correct_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "typed_word_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "suggested_word_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "key_background_pressed_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "suggested_word_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "highlight_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "gesture_trail_color_lxx_light",
                        color3);
                resparam.res.setReplacement(PKG_LATINIME, "color", "sliding_key_input_preview_color_lxx_light",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "sliding_key_input_preview_color_lxx_dark",
                        color2);
                resparam.res.setReplacement(PKG_LATINIME, "color", "gesture_trail_color_lxx_dark",
                        color2);

                Logger.v("Replaced Theme resources for Contacts app.");

            } else if (resparam.packageName.equals(SettingsHelper.PACKAGE_NAME)) {
                Logger.v("Replacing Theme resources for module.");

                resparam.res.setReplacement(SettingsHelper.PACKAGE_NAME, "color", "theme1",
                        color1);
                resparam.res.setReplacement(SettingsHelper.PACKAGE_NAME, "color", "theme2",
                        color2);
                resparam.res.setReplacement(SettingsHelper.PACKAGE_NAME, "color", "theme3",
                        color3);

                Logger.v("Replaced Theme resources for module.");
            } else if (resparam.packageName.equals(PKG_HTC_LAUNCHER)) {
                Logger.v("Replacing string resource for Sense Home.");

                resparam.res.setReplacement(PKG_HTC_LAUNCHER, "string", "msg_full_theme_applied",
                        STRING_REBOOT);

                Logger.v("Replaced string resource for Sense Home.");
            }
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        if (mSettings.getCachedPref_use_themes()) {
            replaceSystemWideThemes();
        } else {
            Logger.v("Themes are turned off in module settings.");
        }

        Logger.v("Loading hook to add HTC features to system feature list...");

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
                        Logger.logHookAfter(param);
                    }
                });

        Logger.v("System feature list hook loaded.");
    }

    private void replaceSystemWideThemes() {
        int color1 = mSettings.getCachedPref_systemui_color1();
        int color2 = mSettings.getCachedPref_systemui_color2();
        int color3 = mSettings.getCachedPref_systemui_color3();
        int color4 = mSettings.getCachedPref_systemui_color4();
        Logger.v("Replacing system-wide Theme resources.");
        Logger.logTheme(mSettings);

        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_900",
                color1);
        XResources.setSystemWideReplacement("android", "color", "primary_material_dark",
                color2);
//        XResources.setSystemWideReplacement("android", "color", "primary_material_light",
//                Common.enlightColor(mSettings.getPrimaryColor(), 2.25f));
        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_950",
                color1);
        XResources.setSystemWideReplacement("android", "color", "primary_dark_material_dark",
                color2);
//        XResources.setSystemWideReplacement("android", "color", "primary_dark_material_light",
//                Common.enlightColor(mSettings.getPrimaryColor(), 1.65f));
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_500",
                color2);
        XResources.setSystemWideReplacement("android", "color", "accent_material_dark",
                color4);
        XResources.setSystemWideReplacement("android", "color", "accent_material_light",
                color3);
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_200",
                color2);

        Logger.v("Theme resources replaced.");
    }
}
