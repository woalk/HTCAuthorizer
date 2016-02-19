package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

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
    private String mPrefKey;

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

    protected void fetchApp(String inputAppName, Context appContext, String prefKey) {
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = context.getString(R.string.url_apkmirror) + "/?s=" + newName + "&post_type=app_release";
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


    protected void fetchApp(String inputAppName, Context appContext, String prefKey, Boolean skipDownload) {

        if (skipDownload) {
            Logger.d("HTMLHelper: Calling fetch for app " + inputAppName);
        }
        //Set our input app name to be URL-friendly
        originalName = inputAppName;
        newName = inputAppName.replaceAll("\\s", "+");
        //Set up search URL
        String holderUrl = context.getString(R.string.url_apkmirror) + "/?s=" + newName + "&post_type=app_release";
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

    private Boolean checkNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return null != activeNetwork && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    protected void doDownloadInstall(final String urlLink) {

        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlLink));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(originalName);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, parsedFileName + ".apk");
        long enqueue = dm.enqueue(request);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean mUseThemes = sharedPreferences.getBoolean("use_themes", false);
        int accentColor;
        if (mUseThemes) {
            accentColor = sharedPreferences.getInt("theme_AccentColor", 0);
        } else {
            accentColor = Color.parseColor("#FF607D8B");
        }
//

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (!checkNetworkState()) {
                Logger.d("HtmlHelper: Download cancelled, no network");
                this.cancel(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {


            // Connect to the web site
            Document document;
            try {

                document = Jsoup.connect(inUrl).get();
                Logger.d("HTMLHelper: Url is " + inUrl);

                // Using Elements to get the class data
                // Check to see if we're on the "scrape" or
                // "download" phase of the process.
                if (!doDownload) {
                    // Specifies to find the first instance of the class
                    // "downloadlink fontBlue", which on this page, is
                    // the first result for the APK we want.
                    Element url = document.select("a[class=downloadLink iconColor]").first();
                    // Grabs the href attribute
                    parsedHtmlNode = url.attr("href");

                } else {
                    // Look for the class that holds the download link.
                    Element url = document.select("a[class=downloadLink iconColor]").first();
                    parsedHtmlNode = "http://www.apkmirror.com" + url.attr("href");
                    document = Jsoup.connect(parsedHtmlNode).get();
                    url = document.select("a[class=btn btn-flat downloadButton]").first();
                    try {
                        parsedHtmlNode = "http://www.apkmirror.com" + url.attr("href");
                    } catch (NullPointerException e) {
                        Logger.e("Error parsing url" + e);
                    }


                    Element versionNumber = document.select("div.appspec-value").first();
                    Element title = document.select("h1[marginZero wrapText app-title fontBlack noHover]").first();
                    // Grabs the href attribute
                    try {
                        String editText = versionNumber.text();

                    // String appTitle = title.text();
                    int StartPos = editText.indexOf("(") + 1;
                    int EndPos = editText.indexOf(")");
                    parsedVersionNumber = editText.substring(0,StartPos - 1);
                    parsedVersionString = editText.substring(StartPos, EndPos);
                    } catch (NullPointerException e) {
                        Logger.e("EditText was empty" + e);
                    }
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
                if ((!mSkipDownload) && (!parsedHtmlNode.equals(""))) {
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
