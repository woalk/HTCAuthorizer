package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * The class to be loaded by Xposed.
 */
public class X_Mod implements IXposedHookLoadPackage {
    public static final String PKG_HTC_LAUNCHER = "com.htc.launcher";
    public static final String PKG_HTC_LIB0 = "com.htc.lib0";
    public static final String PKG_HTC_SOCIALNETWORK_UI = "com.htc.socialnetwork.common.utils.ui";

    public static final String CLASS_BF_HELPER = PKG_HTC_LAUNCHER + ".util.HspUpdateHelper";
    public static final String CLASS_BF_SETTINGUTIL = PKG_HTC_LAUNCHER + ".util.SettingUtil";
    public static final String CLASS_BF_LIB2 = "com.htc.lib2.Hms";
    public static final String CLASS_BF_UDACT = PKG_HTC_SOCIALNETWORK_UI + ".HMSUpdateActivity";
    public static final String CLASS_BF_LOCK = "com.htc.blinklock.BlinkLockProvider";
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
    public static final String CLASS_GPLUS_HMSUPDATE = PKG_HTC_GPLUS + ".HMSUpdateActivity";

    public static final String PKG_HTC_INSTAGRAM_APP = "com.htc.sense.socialnetwork.instagram";
    public static final String PKG_HTC_INSTAGRAM = "com.htc.socialnetwork.instagram";
    public static final String PKG_HTC_LIB2 = "com.htc.lib2";

    public static final String CLASS_HDK0UTIL = PKG_HTC_LIB0 + ".HDKLib0Util";
    public static final String CLASS_BASE_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI + ".BaseActivity";
    public static final String CLASS_COMMON_MF_MAIN_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI +
            ".CommonMfMainActivity";

    public static final String CLASS_INSTAGRAM_ACTIVITY = PKG_HTC_INSTAGRAM + ".InstagramActivity";
    public static final String CLASS_INSTAGRAM_LIB2_A = PKG_HTC_LIB2 + ".a";

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
                XposedBridge.log("HTC Auth: FB hooking now!");

                XposedHelpers.findAndHookMethod(CLASS_FB_BASE_ACTIVITY, lpparam.classLoader,
                        "e", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {

                                param.setResult(true);
                            }
                        });

                XposedBridge.log("HTC Auth: FB is alive! Stab it in the heart!");
            } catch (Throwable e) {
                e.printStackTrace();
                XposedBridge.log("HTC Auth: No FB here.");
            }
        } else if (lpparam.packageName.equals(PKG_HTC_GPLUS_APP)) {

            try {
                XposedBridge.log("HTC Auth: G+ hooking now!");

                XposedHelpers.findAndHookMethod(CLASS_GPLUS_ACTIVITY, lpparam.classLoader,
                        "f", new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {

                                XposedBridge.log("HTC Auth: G+ hook f() called");
                                XposedHelpers.setBooleanField(param.thisObject, "a", true);
                                param.setResult(true);
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_GPLUS_HMSUPDATE, lpparam.classLoader,
                        "onCreate", Bundle.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                XposedBridge.log("HTC Auth: G+ hook onCreate(Bundle) called");
                                ((Activity) param.thisObject).getIntent().setAction("ANY_ACTION");
                            }
                        });

                XposedBridge.log("HTC Auth: G+ is alive!");
            } catch (Throwable e) {
                e.printStackTrace();
                XposedBridge.log("HTC Auth: No G+ here.");
            }
        } else if (lpparam.packageName.equals(PKG_HTC_INSTAGRAM_APP)) {

            try {
                XposedBridge.log("HTC Auth: Insta hooking now!");

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "a",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                param.setResult(7.0f);
                                XposedBridge.log("HTC Auth: Insta hook a() called");
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_LIB2_A, lpparam.classLoader, "b",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                XposedBridge.log("HTC Auth: Insta hook b() called");
                            }
                        });

                XposedHelpers.findAndHookMethod(CLASS_INSTAGRAM_ACTIVITY, lpparam.classLoader, "d",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws
                                    Throwable {
                                param.setResult(true);
                                XposedBridge.log("HTC Auth: Insta hook d() called");
                            }
                        });

                XposedBridge.log("HTC Auth: Insta is alive!");
            } catch (Throwable e) {
                e.printStackTrace();
                XposedBridge.log("HTC Auth: No Insta here.");
            }

        } else if (lpparam.packageName.equals(PKG_HTC_CAMERA)) {

            XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                    "hasRemovableStorageSlot", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });
/*
            XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                    "getRemovableStorageDirectory", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult("/external_sd");
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_HTC_LIB3, lpparam.classLoader,
                    "getRemovableStorageState", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult("MOUNTED");
                        }
                    });
*/
        }
    }


}
