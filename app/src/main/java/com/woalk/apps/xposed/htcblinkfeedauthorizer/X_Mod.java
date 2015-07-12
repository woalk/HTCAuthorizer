package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * The class to be loaded by Xposed.
 */
public class X_Mod implements IXposedHookLoadPackage {
    public static final String PKG_HTC_LAUNCHER = "com.htc.launcher";
	public static final String PKG_HTC_GALLERY = "com.htc.album";
	public static final String PKG_HTC_CAMERA = "com.htc.camera";
	public static final String PKG_HTC_LIB0 = "com.htc.lib0";
    public static final String PKG_HTC_LIB2 = "com.htc.lib2.Hms";
	public static final String PKG_HTC_SOCIALNETWORK_UI = "com.htc.socialnetwork.common.utils.ui";

    public static final String CLASS_HDK0UTIL = PKG_HTC_LIB0 + ".HDKLib0Util";
    public static final String CLASS_BASE_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI + ".BaseActivity";
    public static final String CLASS_COMMON_MF_MAIN_ACTIVITY = PKG_HTC_SOCIALNETWORK_UI +
            ".CommonMfMainActivity";
    public static final String CLASS_3DSCENE = "com.htc.sunny2.frameworks.base.widgets.SunnyScene";
	
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		// First section contains common checks found in all HTC Apps
		// Need to see if OR statements are best, or if we can just check for com.htc.* apps
        if (lpparam.packageName.equals(PKG_HTC_LAUNCHER) || lpparam.packageName.equals(PKG_HTC_GALLERY)) {
            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "getSenseVersion",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(7.0f);
                        }
                    });
					
			XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "getHDKBaseVersion",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(19.2f); //Guessing at this value, need to investigate
                        }
                    });

            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isHTCDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });
			
							
			XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isStockUIDevice",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.FALSE);
                        }
                    });
				//I suspect this is critical
				XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isHDKLib3SupportedInDevice",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });

		} else if (lpparam.packageName.equals(PKG_HTC_LAUNCHER)) {

            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isHEPDevice",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });

			XposedHelpers.findAndHookMethod(PKG_HTC_LIB2, lpparam.classLoader, "isHTCDevice",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });
					
            XposedHelpers.findAndHookMethod(CLASS_HDK0UTIL, lpparam.classLoader, "isODMDevice",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.FALSE);
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
        } else if (lpparam.packageName.equals(PKG_HTC_GALLERY)) {
			//This check enables duo fx - tie to a toggle in UI
			XposedHelpers.findAndHookMethod(CLASS_3DSCENE, lpparam.classLoader,
                "enable3dScene", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                        }
                    });

		}
	}
}
