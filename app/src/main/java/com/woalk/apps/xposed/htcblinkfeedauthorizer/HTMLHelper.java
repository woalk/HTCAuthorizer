package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class HTMLHelper {


    private static String TAG = "HTMLHelper: ";
    private String inUrl, originalName, newName, parsedHtmlNode;
    public static String parsedVersionNumber, parsedFileName;
    public static int parsedVersionCode;
    private Boolean doDownload;
    private Boolean mSkipDownload = false;
    private Context context;
    private File dir;
    private int id;
    private Preference mPreference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public HTMLHelper() {

    }


    /**
     * fetchApp
     * <p/>
     * Method to search APKMirror.com for an apk and trigger
     * an automatic download.
     *
     * @param inputAppName Input name of the app to download.
     *                     Can be formatted in plain-text "Sense Home".
     * @param appContext   Context for firing the install command for
     *                     resulting apk.
     */

    protected void fetchApp(String inputAppName, Context appContext, int appIndex, Preference preference) {
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        id=appIndex;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = "http://www.apkmirror.com/?s=" + newName + "&post_type=apps_post";
        //Set input app name for a Filename
        newName = inputAppName.replaceAll("\\s", "");
        //Set the input URL for the background task.
        inUrl = holderUrl;
        //Set "download" boolean to false for intial string.
        doDownload = false;
        //Get a context for intents
        context = appContext;
        //Set up and fire Jsoup task
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        mPreference = preference;
        mSkipDownload = false;
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }


    protected void fetchApp(String inputAppName, Context appContext, int appIndex, Preference preference, Boolean skipDownload) {
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        id=appIndex;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = "http://www.apkmirror.com/?s=" + newName + "&post_type=apps_post";
        //Set input app name for a Filename
        newName = inputAppName.replaceAll("\\s", "");
        //Set the input URL for the background task.
        inUrl = holderUrl;
        //Set "download" boolean to false for intial string.
        doDownload = false;
        //Get a context for intents
        context = appContext;
        //Set up and fire Jsoup task
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        mPreference = preference;
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        mSkipDownload = skipDownload;
        jsoupAsyncTask.execute();

    }


    protected void doDownloadInstall(final String urlLink, final String fileName) {

        Thread dx = new Thread() {

            public void run() {
                final NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setTicker("");
                mBuilder.setContentTitle("Downloading " + originalName)
                        .setContentText("Download in progress")
                        .setSmallIcon(R.drawable.dl_anim);

                //Set up download path
                File root = android.os.Environment.getExternalStorageDirectory();
                dir = new File(root.getAbsolutePath() + "/Download/");


                try {
                    // Open connection to download file.
                    URL url = new URL(urlLink);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // this will be useful so that you can show a typical 0-100% progress bar
                    int fileLength = connection.getContentLength();


                    Logger.d(TAG + "FileLength is " + fileLength + " and filename is " + parsedFileName + ".apk");

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(dir + "/" + parsedFileName + ".apk");

                    byte data[] = new byte[1024];
                    long total = 0;
                    long total2 = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // Set up the current filesize as a percentage
                        double mPercentage = 100.0 * total / fileLength;
                        total += count;
                        // We have to make sure to only update the notification
                        // after so many blocks, else it bogs down SystemUI.
                        if ((total - total2) > 40000) {
                            mBuilder.setProgress(100, (int) Math.round(mPercentage), false);
                            mNotifyManager.notify(id, mBuilder.build());
                            total2 = total;
                        }

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                    notificationIntent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + parsedFileName + ".apk")), "application/vnd.android.package-archive");
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

                    PendingIntent intent = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);


                    mBuilder.setContentTitle("Download Complete")
                            .setContentText("Tap to install " + originalName)
                            .setProgress(0, 0, false)
                            .setSmallIcon(R.drawable.stat_sys_download_anim0)
                            .setContentIntent(intent);

                    mNotifyManager.notify(id, mBuilder.build());



                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };

        //Start the thread
        dx.start();
    }





    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            // Connect to the web site
            Document document;
            try {
                document = Jsoup.connect(inUrl).get();

                // Using Elements to get the class data
                // Check to see if we're on the "scrape" or
                // "download" phase of the process.
                if (!doDownload) {
                    // Specifies to find the first instance of the class
                    // "downloadlink fontBlue", which on this page, is
                    // the first result for the APK we want.
                    Element url = document.select("a[class=downloadlink fontBlue]").first();
                    // Grabs the href attribute
                    parsedHtmlNode = url.attr("href");
                    Logger.d(TAG + "Parsed url is " + parsedHtmlNode);

                } else {
                    // Look for the class that holds the download link.
                    Element url = document.select("a[class=waves-effect waves-button downloadButton]").first();
                    Element versionNumber = document.select("h1[class=marginZero wrapText apk-title]").first();
                    Element versionCode = document.select("span[class=  fontBlue]").first();
                    // Grabs the href attribute
                    parsedHtmlNode = url.attr("href");
                    parsedVersionNumber = versionNumber.attr("title");
                    parsedFileName = parsedVersionNumber.replaceAll("\\s", "_");
                    parsedVersionNumber = parsedVersionNumber.replaceAll("[^0-9.]", "");
                    String parsedVersionString = versionCode.childNode(0).toString().replace(parsedVersionNumber,"");
                    parsedVersionString = parsedVersionString.substring(1,4);
                    Logger.d(TAG + "Version code is " + parsedVersionString);
                    editor.putString(mPreference.getKey(), parsedVersionNumber);
                    editor.apply();
                    Logger.d(TAG + "Parsed url for DL is " + parsedHtmlNode);
                    Logger.d(TAG + "Putting version number for preference of " + mPreference.getKey() + " and " + parsedVersionNumber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override

        // * onPostExecute is where we set the doDownload boolean
        // * **
        // * If it is set, we will pass the URL output from our
        // * doInBackground task to the doDownloadInstall method,
        // * which will download and install the file.
        // * **
        // * Otherwise, we will set the input URL (inURL) to our
        // * background task, which will then parse and scrape for
        // * the actual download link.
        // *
        // * *****
        protected void onPostExecute(Void result) {
            if (doDownload) {
                Logger.d(TAG + "Starting download for url of " + parsedHtmlNode);
                if (!mSkipDownload) {
                    mSkipDownload = true;
                    doDownloadInstall(parsedHtmlNode, newName + ".apk");
                }
                doDownload = false;
            } else {
                inUrl = "http://www.apkmirror.com" + parsedHtmlNode;
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

                    doDownload = true;

            }

        }
    }
}
