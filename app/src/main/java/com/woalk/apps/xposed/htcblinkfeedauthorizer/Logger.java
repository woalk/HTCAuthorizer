package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.util.Log;

/**
 * Class for general logging and other debug methods.
 */
@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class Logger {
    private Logger() {
    }

    public static final String LOG_TAG = "Sensify";
    public static final boolean DO_LOG = true;
    public static final boolean EXCLUDE_ERRORS = true;

    public static void v(String msg, Object... args) {
        if (!DO_LOG) return;
        if (args.length == 1 && args[0] instanceof Throwable) {
            Log.v(LOG_TAG, msg, (Throwable) args[0]);
        } else {
            Log.v(LOG_TAG, String.format(msg, args));
        }
    }

    public static void d(String msg, Object... args) {
        if (!DO_LOG) return;
        if (args.length == 1 && args[0] instanceof Throwable) {
            Log.d(LOG_TAG, msg, (Throwable) args[0]);
        } else {
            Log.d(LOG_TAG, String.format(msg, args));
        }
    }

    public static void i(String msg, Object... args) {
        if (!DO_LOG) return;
        if (args.length == 1 && args[0] instanceof Throwable) {
            Log.i(LOG_TAG, msg, (Throwable) args[0]);
        } else {
            Log.i(LOG_TAG, String.format(msg, args));
        }
    }

    public static void w(String msg, Object... args) {
        if (!DO_LOG) return;
        if (args.length == 1 && args[0] instanceof Throwable) {
            Log.w(LOG_TAG, msg, (Throwable) args[0]);
        } else {
            Log.w(LOG_TAG, String.format(msg, args));
        }
    }

    public static void e(String msg, Object... args) {
        if (!DO_LOG && !EXCLUDE_ERRORS) return;
        if (args.length == 1 && args[0] instanceof Throwable) {
            Log.e(LOG_TAG, msg, (Throwable) args[0]);
        } else {
            Log.e(LOG_TAG, String.format(msg, args));
        }
    }

    public static void logTheme(SettingsHelper settings) {
        v("Theme is: %s", settings.theme_toString());
    }
}
