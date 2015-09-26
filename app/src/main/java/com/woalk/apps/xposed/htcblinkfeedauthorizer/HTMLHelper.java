package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

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
    private String inUrl, newName, parsedHtmlNode;
    private Boolean doDownload;
    private Context context;
    private File dir;

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

    protected void fetchApp(String inputAppName, Context appContext) {
        //Set our input app name to be URL-friendly
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
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }


    protected void doDownloadInstall(final String urlLink, final String fileName) {
        Thread dx = new Thread() {

            public void run() {
                //Set up download path
                File root = android.os.Environment.getExternalStorageDirectory();
                dir = new File(root.getAbsolutePath() + "/Sensify/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                try {
                    // Open connection to download file.
                    URL url = new URL(urlLink);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // this will be useful so that you can show a typical 0-100% progress bar
                    int fileLength = connection.getContentLength();

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(dir + "/" + fileName);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();

                    // Start an intent to install our file
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Sensify/" + newName + ".apk")), "application/vnd.android.package-archive");
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
                    context.startActivity(i);

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
            Document document = null;
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
                    // Grabs the href attribute
                    parsedHtmlNode = url.attr("href");
                    Logger.d(TAG + "Parsed url for DL is " + parsedHtmlNode);
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
                doDownloadInstall(parsedHtmlNode, newName + ".apk");
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
