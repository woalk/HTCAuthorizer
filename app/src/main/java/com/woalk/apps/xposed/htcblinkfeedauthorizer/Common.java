package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xmlpull.v1.XmlSerializer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class Common {
    public static final String versionName = "3.0";
    public static final String xhtag = "Common: ";
    static String path = "/Sensify";
    static File directory = new File(Environment.getExternalStorageDirectory().toString() + path);
    static String permfilename = "/com.htc.software.market.xml";
    static File permfile = new File(directory + permfilename);
    protected Context context;

    public Common() {

    }

    public Common(Context appcontext) {
        context = appcontext;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Searches a {@link ViewGroup} for the first {@link ImageView} it can find in its child views.
     *
     * @param container The container {@link ViewGroup}.
     * @return The first {@link ImageView} in the {@code container ViewGroup} (counted by child
     * indexes), or {@code null} if no {@code ImageView} was found.
     */
    public static ImageView findFirstImageView(ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            if (v instanceof ImageView) {
                return (ImageView) v;
            } else if (v instanceof ViewGroup) {
                return findFirstImageView((ViewGroup) v);
            }
        }
        return null;
    }

    public static ImageView findLastImageView(ViewGroup container) {

        View v = container.getChildAt(container.getChildCount());

        if (v instanceof ImageView) {
            return (ImageView) v;
        } else {
            return null;
        }

    }


    public static int lightenColor(int color, float modifier) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);
        hsv[2] = hsv[2] + modifier;
        return Color.HSVToColor(hsv);
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void killPackage(final String packageToKill) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Runtime.getRuntime().exec(new String[]{"su", "-c", "pkill " + packageToKill});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static void fixPermissions(Context context) {
        File theSharedPrefsFile;
        String PACKAGE_NAME = "com.woalk.apps.xposed.htcblinkfeedauthorizer";
        String PREFERENCE_FILE = PACKAGE_NAME + "_preferences";

        try {
            Logger.d("Common: Fixperms called.");
            String path = context.getFilesDir().getPath();
            path = path.replace("/files", "");
            theSharedPrefsFile = new File(path + "/shared_prefs/" + PREFERENCE_FILE + ".xml");
            theSharedPrefsFile.setReadable(true, false);
        } catch (Exception e) {
            Logger.e("LaunchActivity: Fuck: " + e);
        }
    }

    public static boolean checkPermFileExists() {
        File sysfile = new File("/system/etc/permissions/com.htc.software.market.xml");
        return sysfile.exists();

    }

    public static void createPermFile() {
        if (!permfile.exists()) try {
            File dirs = new File(directory.getPath());
            dirs.mkdirs();
            Logger.d("Sensify: Creating permission file " + permfile.getPath());

            FileOutputStream fileos = new FileOutputStream(permfile);
            XmlSerializer xmlSerializer = Xml.newSerializer();

            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", null);
            xmlSerializer.startTag(null, "permissions");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.HTC");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.Sense7.0");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.M8WHL");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.IHSense");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.hdk");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.hdk2");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.startTag(null, "feature");
            xmlSerializer.attribute("", "name", "com.htc.software.hdk3");
            xmlSerializer.endTag(null, "feature");
            xmlSerializer.endTag(null, "permissions");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            Logger.d(xhtag + "Permission file Successfully created.");


        } catch (IOException e) {
            Logger.e(xhtag + "error " + e);

        }
        else {
            Logger.d(xhtag + "permission file " + permfile.getPath() + " already exists.");

        }
    }

    public static void runAsRoot(final String[] cmds) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Process p = Runtime.getRuntime().exec("su");
                    Logger.d("Common: runAsRoot recieved paramater " + Arrays.toString(cmds));
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());

                    for (String tmpCmd : cmds) {
                        Logger.d("Common: runAsRoot executing command " + tmpCmd);
                        os.writeBytes(tmpCmd + "\n");
                        os.flush();
                    }

                    os.writeBytes("exit\n");
                    os.flush();
                    p.waitFor();
                    p.destroy();
                } catch (IOException | InterruptedException e) {
                    Logger.e("Common: Error with SU - " + e);
                }
                return null;
            }

        }.execute();
    }

    public static void runAsRoot(final String cmd) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Process p = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());


                        Logger.d("Common: runAsRoot executing command " + cmd);
                        os.writeBytes(cmd + "\n");
                        os.flush();


                    os.writeBytes("exit\n");
                    os.flush();
                    p.waitFor();
                    p.destroy();
                } catch (IOException | InterruptedException e) {
                    Logger.e("Common: Error with SU - " + e);
                }
                return null;
            }

        }.execute();
    }

    public boolean copyPermFile() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Sensify/com.htc.software.market.xml");
        File sysfile = new File("/system/etc/permissions/com.htc.software.market.xml");
        if (!sysfile.exists()) {
            String[] cmd = {"mount -o remount,rw /system", "cp " + file + " " + sysfile, "chmod 0644 " + sysfile, "mount -o remount,ro /system"};
            Logger.d("Common: passing command to root - " + Arrays.toString(cmd));
            runAsRoot(cmd);
            return true;

        } else {
            Logger.d("Common: Sysfile exists.");
            return false;

        }


    }

    public boolean copyPermFile(boolean force) {

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Sensify/com.htc.software.market.xml");
        File sysfile = new File("/system/etc/permissions/com.htc.software.market.xml");
        if (force) {
            String[] cmd = {"mount -o remount,rw /system", "cp " + file + " " + sysfile, "chmod 0644 " + sysfile, "mount -o remount,ro /system"};
            Logger.d("Common: passing command to root - " + Arrays.toString(cmd));
            runAsRoot(cmd);
            return true;

        } else {
            Logger.d("Common: Sysfile exists.");
            return false;

        }


    }
}
