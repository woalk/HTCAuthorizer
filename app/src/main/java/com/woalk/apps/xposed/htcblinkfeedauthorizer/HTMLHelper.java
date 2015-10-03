package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;


public class HTMLHelper {


    public static String parsedVersionNumber, parsedFileName, parsedVersionString;
    private String inUrl, originalName, newName, parsedHtmlNode, versionString;
    private Boolean doDownload;
    private Boolean mSkipDownload = false;
    private Context context;
    private int id, accentColor;
    private String mPrefKey;
    private long enqueue;

    public HTMLHelper(Context appcontext) {
        context = appcontext;
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

    protected void fetchApp(String inputAppName, Context appContext, int appIndex, String prefKey) {
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        id = appIndex;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = context.getString(R.string.url_apkmirror) + "/?s=" + newName + "&post_type=apps_post";
        //Set input app name for a Filename
        newName = inputAppName.replaceAll("\\s", "");
        //Set the input URL for the background task.
        inUrl = holderUrl;
        //Set "download" boolean to false for intial string.
        doDownload = false;
        //Get a context for intents
        context = appContext;
        //Set up and fire Jsoup task
        mPrefKey = prefKey;
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }


    protected void fetchApp(String inputAppName, Context appContext, int appIndex, String prefKey, Boolean skipDownload) {
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        id = appIndex;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = context.getString(R.string.url_apkmirror) + "/?s=" + newName + "&post_type=apps_post";
        //Set input app name for a Filename
        newName = inputAppName.replaceAll("\\s", "");
        //Set the input URL for the background task.
        inUrl = holderUrl;
        //Set "download" boolean to false for intial string.
        doDownload = false;
        //Get a context for intents
        context = appContext;
        //Set up and fire Jsoup task
        mPrefKey = prefKey;
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        mSkipDownload = skipDownload;
        jsoupAsyncTask.execute();

    }


    protected void doDownloadInstall(final String urlLink) {
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlLink));
        request.setTitle(originalName);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, parsedFileName + ".apk");
        enqueue = dm.enqueue(request);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean mUseThemes = sharedPreferences.getBoolean("use_themes", false);
        if (mUseThemes) {
            accentColor = sharedPreferences.getInt("theme_AccentColor", 0);
        } else {
            accentColor = Color.parseColor("#FF607D8B");
        }
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = c.getInt(columnIndex);
                        int columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int reason = c.getInt(columnReason);
                        c.close();
                        if (DownloadManager.STATUS_SUCCESSFUL == reason) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                            notificationIntent.setDataAndType((Uri.parse(uriString)), "application/vnd.android.package-archive");
                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                            mBuilder.setContentTitle(context.getString(R.string.msg_download_complete_title))
                                    .setContentText(context.getString(R.string.msg_download_complete_body) + originalName)
                                    .setProgress(0, 0, false)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.stat_sys_download_anim0)
                                    .setColor(accentColor)
                                    .setContentIntent(pendingIntent);
                            mNotifyManager.notify(id, mBuilder.build());

                        } else {
                            switch (status) {
                                case DownloadManager.STATUS_FAILED:
                                    mBuilder.setContentTitle(context.getString(R.string.msg_download_failed_title));
                                    switch (reason) {
                                        case DownloadManager.ERROR_CANNOT_RESUME:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_cannot_resume_body));
                                            break;
                                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_not_found_body));
                                            break;
                                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_already_exists_body));
                                            break;
                                        case DownloadManager.ERROR_FILE_ERROR:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_file_error_body));
                                            break;
                                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_http_error_body));
                                            break;
                                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_low_space_body));
                                            break;
                                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                            mBuilder.setContentText(context.getString(R.string.msg_body_toomany_redirects_body));
                                            break;
                                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_unhandled_error_body));
                                            break;
                                        case DownloadManager.ERROR_UNKNOWN:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_unknown_error_body));
                                            break;
                                    }


                                case DownloadManager.STATUS_PAUSED:
                                    mBuilder.setContentText(context.getString(R.string.msg_download_paused_title));
                                    switch (reason) {
                                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_paused_queued_body));
                                            break;
                                        case DownloadManager.PAUSED_UNKNOWN:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_paused_unknown));
                                            break;
                                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_paused_waiting_network_body));
                                            break;
                                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                            mBuilder.setContentText(context.getString(R.string.msg_download_paused_waiting_retry_body));
                                            break;
                                    }
                                    break;
                                case DownloadManager.STATUS_PENDING:
                                    break;
                                case DownloadManager.STATUS_RUNNING:
                                    break;

                            }
                            mNotifyManager.notify(id, mBuilder.build());
                        }
                    }
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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

                } else {
                    // Look for the class that holds the download link.
                    Element url = document.select("a[class=waves-effect waves-button downloadButton]").first();
                    Element versionNumber = document.select("h1[class=marginZero wrapText apk-title]").first();
                    Elements versionCode = document.select("span[class=  fontBlue]");
                    // Grabs the href attribute
                    parsedHtmlNode = url.attr("href");
                    parsedVersionNumber = versionNumber.attr("title");
                    parsedFileName = parsedVersionNumber.replaceAll("\\s", "_");
                    parsedVersionNumber = parsedVersionNumber.replaceAll("[^0-9.]", "");
                    for (Element element : versionCode) {
                        if (element.ownText().equals("Version:")) {
                            Node node = element.nextSibling();
                            versionString = node.toString();

                            break;
                        }
                    }

                    parsedVersionString = versionString.replace(parsedVersionNumber, "");
                    parsedVersionString = parsedVersionString.substring(2, parsedVersionString.length() - 1);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(mPrefKey, parsedVersionNumber);
                    editor.putInt(mPrefKey + "_code", Integer.valueOf(parsedVersionString));
                    editor.apply();
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
                if (!mSkipDownload) {
                    mSkipDownload = true;
                    doDownloadInstall(parsedHtmlNode);
                } else {
                    Intent intent = new Intent("com.woalk.HTCAuthorizer.VERSION_FETCHED");
                    intent.putExtra("version_code", Integer.valueOf(parsedVersionString));
                    context.sendBroadcast(intent);
                }
                doDownload = false;
            } else {
                inUrl = context.getString(R.string.url_apkmirror) + parsedHtmlNode;
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

                doDownload = true;

            }

        }
    }
}
