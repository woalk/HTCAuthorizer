package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XResources;
import android.graphics.Color;
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

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
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

    public static final String PKG_HTC_CAMERA = "com.htc.camera";
    public static final String CLASS_HTC_LIB3 = "com.htc.lib3.android.os.HtcEnvironment";

    public static final String PKG_HTC_GALLERY = "com.htc.album";
    public static final String CLASS_3DSCENE = "com.htc.sunny2.frameworks.base.widgets.SunnyScene";

    public static final String PKG_HTC_FB = "com.htc.sense.socialnetwork.facebook";
    public static final String CLASS_FB_BASE_ACTIVITY = PKG_HTC_FB + ".FacebookBaseActivity";

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


    public static final String CLASS_HDK0UTIL = PKG_HTC_LIB0 + ".HDKLib0Util";
    public static final String CLASS_BASE_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI + ".BaseActivity";
    public static final String CLASS_COMMON_MF_MAIN_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI +
            ".CommonMfMainActivity";

    public static final String CLASS_INSTAGRAM_ACTIVITY = PKG_HTC_INSTAGRAM_COMM +
            ".InstagramActivity";
    public static final String CLASS_INSTAGRAM_LIB2_A = PKG_HTC_LIB2 + ".a";

    private final SettingsHelper mSettings;

    public X_Mod() {
        XposedBridge.log("HTC Auth: Created preference instance.");
        mSettings = new SettingsHelper();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // First section contains common checks found in all HTC Apps
        // Need to see if OR statements are best, or if we can just check for com.htc.* apps
        if (lpparam.packageName.equals(PKG_HTC_LAUNCHER)) {

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                     new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_HELPER, lpparam.classLoader, "isHSPCompatible",
                    Context.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_SETTINGUTIL, lpparam.classLoader,
                    "isHtcDevice", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isHTCDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_LIB2, lpparam.classLoader, "isHtcDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BASE_ACTIVITY, lpparam.classLoader,
                    "checkCompatibility", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_COMMON_MF_MAIN_ACTIVITY, lpparam.classLoader,
                    "checkCompatibility", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_UDACT, lpparam.classLoader, "onCreate",
                    Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            ((Activity) param.thisObject).getIntent().setAction("ANY_ACTION");
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_PROFILEBRIEF, lpparam.classLoader,
                    "isHtc", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_BF_PROFILEBRIEF, lpparam.classLoader, "isHtc",
                    CLASS_BF_PROFILEBRIEF, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

        } else if (lpparam.packageName.equals(PKG_HTC_FB)) {

            try {
                XposedHelpers.findAndHookMethod(CLASS_FB_BASE_ACTIVITY, lpparam.classLoader,
                        "e", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else if (lpparam.packageName.equals(PKG_HTC_GPLUS_APP)) {

            try {
                XposedHelpers.findAndHookMethod(CLASS_GPLUS_ACTIVITY, lpparam.classLoader,
                        "f", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_GPLUS_DEEPLINK_ACTIVITY, lpparam.classLoader,
                        "d", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else if (lpparam.packageName.equals(PKG_HTC_INSTAGRAM)) {

            try {
                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                param.setResult(7.0f);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_ACTIVITY, lpparam.classLoader, "d",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "b_", true);
                                param.setResult(true);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else if (lpparam.packageName.equals(PKG_HTC_LINKEDIN)) {

            try {
                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_LIB2_A, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                param.setResult(7.0f);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_LINKEDIN_ACTIVITY, lpparam.classLoader, "g",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else if (lpparam.packageName.equals(PKG_HTC_TWITTER)) {

            try {
                XposedHelpers.findAndHookMethod(CLASS_TWITTER_ACTIVITY, lpparam.classLoader, "d",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_TWITTER_DEEPLINK_ACTIVITY,
                        lpparam.classLoader, "b", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else if (lpparam.packageName.equals(PKG_SETTINGS)
                && mSettings.getCachedPref_use_themes()) {
            XposedHelpers.findAndHookMethod(Preference.class, "setIcon", Drawable.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Drawable d = (Drawable) param.args[0];
                            BitmapDrawable b = new BitmapDrawable(((Preference) param.thisObject)
                                    .getContext().getResources(), Common.drawableToBitmap(d));
                            b.setTint(mSettings.getThemeColor(2));
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
                            b.setTint(mSettings.getThemeColor(2));
                            iV.setImageDrawable(b);
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
                            b.setTint(mSettings.getThemeColor(2));
                            iV.setImageDrawable(b);
                        }
                    });
        }

        if (lpparam.packageName.startsWith("com.htc.")) {

            // Following: HTC-specific methods that resolve different storage types
            // try-catch for each necessary because not every HTC app uses all of them

            // Phone storage (non-removable) => internal sdcard
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasPhoneStorage", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                // Checks PRIMARY "external" storage.
                                // Android recognizes INTERNAL sdcard as primary external storage.
                                // If no internal sdcard, primary storage would be real sdcard.
                                param.setResult(!Environment.isExternalStorageRemovable());
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getPhoneStorageDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(!Environment.isExternalStorageRemovable() ?
                                        Environment.getExternalStorageDirectory()
                                        : null);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getPhoneStorageState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(Environment.isExternalStorageRemovable() ?
                                        Environment.getExternalStorageState()
                                        : Environment.MEDIA_UNKNOWN);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

            // Removable storage => external sdcard
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasRemovableStorageSlot", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_ext());
                                XposedBridge.log(String.valueOf(mSettings.getPref_has_ext()));
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getRemovableStorageDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_ext() ?
                                        new File(mSettings.getPref_ext_path()) : null);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getRemovableStorageState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_ext() ?
                                        Environment.getExternalStorageState(
                                                new File(mSettings.getPref_ext_path()))
                                        : Environment.MEDIA_UNKNOWN);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }

            // USB storage (OTG)
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "hasUsbDeviceSlot", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_usb());
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getUsbDeviceDirectory", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_usb() ?
                                        new File(mSettings.getPref_usb_path()) : null);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                        "getUsbDeviceState", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                param.setResult(mSettings.getPref_has_usb() ?
                                        Environment.getExternalStorageState(
                                                new File(mSettings.getPref_usb_path()))
                                        : Environment.MEDIA_UNKNOWN);
                            }
                        });
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static final String PKG_SYSTEMUI = "com.android.systemui";
    public static final String PKG_SETTINGS = "com.android.settings";
    public static final String CLASS_SETTINGS_DASHBOARD_SUMMARY = PKG_SETTINGS +
            ".dashboard.DashboardSummary";
    public static final String CLASS_SETTINGS_DASHBOARD_TILE = PKG_SETTINGS +
            ".dashboard.DashboardTile";
    public static final String CLASS_SETTINGS_SEARCH_RESULTS_SUMMARY = PKG_SETTINGS +
            ".dashboard.SearchResultsSummary";
    public static final String CLASS_SETTINGS_SEARCH_RESULTS_ADAPTER =
            CLASS_SETTINGS_SEARCH_RESULTS_SUMMARY + "$SearchResultsAdapter";

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam
                                                   resparam) throws Throwable {
        if (!mSettings.getCachedPref_use_themes())
            return;

        if (resparam.packageName.equals(PKG_SYSTEMUI)) {
            XposedBridge.log("SystemUI resources replaced.");
            resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_primary_color",
                    mSettings.getCachedPref_systemui_color2());
            resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_secondary_color",
                    mSettings.getCachedPref_systemui_color1());
            resparam.res.setReplacement(PKG_SYSTEMUI, "color", "system_accent_color",
                    mSettings.getThemeColor(2));
        } else if (resparam.packageName.equals(PKG_SETTINGS)) {
            XposedBridge.log("Settings resources replaced.");
            resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary",
                    mSettings.getThemeColor(3));
            resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_primary_dark",
                    Common.enlightColor(mSettings.getThemeColor(3), 0.6f));
            resparam.res.setReplacement(PKG_SETTINGS, "color", "theme_accent",
                    mSettings.getThemeColor(2));
            resparam.res.setReplacement(PKG_SETTINGS, "color", "switchbar_background_color",
                    mSettings.getThemeColor(3));
            resparam.res.setReplacement(PKG_SETTINGS, "color", "switch_accent_color",
                    mSettings.getThemeColor(4));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        if (!mSettings.getCachedPref_use_themes())
            return;

        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_900",
                mSettings.getThemeColor(3));
        XResources.setSystemWideReplacement("android", "color", "material_blue_grey_950",
                Common.enlightColor(mSettings.getThemeColor(3), 0.6f));
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_500",
                mSettings.getThemeColor(2));
        XResources.setSystemWideReplacement("android", "color", "material_deep_teal_200",
                Common.enlightColor(mSettings.getThemeColor(1), 1.5f));
    }
}
