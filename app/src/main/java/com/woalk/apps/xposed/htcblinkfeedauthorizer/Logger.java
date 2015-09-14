package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.robv.android.xposed.XC_MethodHook;

/**
 * Class for general logging and other debug methods.
 */
@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class Logger {
    public static final String LOG_TAG = "Sensify";
    public static final boolean DO_LOG = true;
    public static final boolean EXCLUDE_ERRORS = true;

    private Logger() {
    }

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

    public static void logStart() {
        v("------------------------------");
        v("Sensify startup. X_Mod loaded.");
        v("Version %s", Common.versionName);
        v("by Woalk & Digitalhigh");
        v("------------------------------");
    }


    public static void logHook(XC_MethodHook.MethodHookParam param) {
        v("Method hook %s/.%s executed, pre-result=%s;args=%s",
                param.method.getDeclaringClass().getName(), param.method.getName(),
                param.getResult(), getArrayString(param.args));
    }

    public static void logHookAfter(XC_MethodHook.MethodHookParam param) {
        v("Method hook %s/.%s executed, post-result=%s;args=%s",
                param.method.getDeclaringClass().getName(), param.method.getName(),
                param.getResult(), getArrayString(param.args));
    }

    public static String getArrayString(Object[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Object o : array) {
            sb.append(o)
                    .append(",");
        }
        sb.append("}");
        return sb.toString();
    }


    // v---- READING ----v \\

    public static void readLogcat(final WebView tv) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Process process = Runtime.getRuntime().exec(new String[]{"su", "-c",
                            "logcat -d -s Sensify:*"});
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    Thread.sleep(5);

                    StringBuilder log = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        int bracket = line.indexOf(")") + 1;
                        String color = "grey";
                        boolean allLineColor = false;
                        if (line.startsWith("W/")) {
                            color = "orange";
                            allLineColor = true;
                        } else if (line.startsWith("E/")) {
                            color = "red";
                            allLineColor = true;
                        }
                        log.append("<font color=\"")
                                .append(color)
                                .append("\">")
                                .append(line.substring(0, bracket))
                                .append(allLineColor ? "<strong>" : "</font>")
                                .append(line.substring(bracket))
                                .append(allLineColor ? "</strong></font>" : "")
                                .append("<br/>");
                    }
                    return log.toString();
                } catch (Throwable e) {
                    e("Could not read log file in module.", e);
                    return "Could not read log file.\n" + e.toString();
                }
            }

            @Override
            protected void onPostExecute(String html) {
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                tv.loadDataWithBaseURL("", html, mimeType, encoding, "");
            }
        }.execute();
    }

    public static String saveLogcat() {
        String path = "/Sensify";
        File directory = new File(Environment.getExternalStorageDirectory().toString() + path);
        String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        final File file = new File(directory, "logcat-" + date + ".txt");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (Common.isExternalStorageWritable()) {
                        Process process = Runtime.getRuntime().exec(new String[]{"su", "-c",
                                "logcat -d -v time"});
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));
                        OutputStream os = new FileOutputStream(file);

                        Thread.sleep(5);

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            os.write((line + "\n").getBytes(StandardCharsets.UTF_8));
                        }
                        os.close();
                    }
                    return null;
                } catch (Throwable e) {
                    e("Could not save log in module.", e);
                    return null;
                }
            }
        }.execute();
        return file.getAbsolutePath();
    }
}
