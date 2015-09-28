package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



public class DownloadPreference extends Preference {

    public static String mPackageName;
    private String pName;
    private String mPackageVersionName;
    private String mKey;
    private String mTitleText;
    private String mSummaryText;
    private int mPackageVersionInstalled, mPackageVersionAvailable;
    private Boolean mIsInstalled;
    private SharedPreferences sharedPreferences;
    private TextView mTitle, mSummary;

    public DownloadPreference(Context context, AttributeSet attrs) {
        super(context);
        init(context, attrs);
    }

    public DownloadPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public DownloadPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }



    @Override
    protected View onCreateView(ViewGroup parent) {
        this.setLayoutResource(R.layout.pref_download);
        return super.onCreateView(parent);

    }

    @Override
    public CharSequence getTitle() {
        return mTitle.getText();
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    protected void onBindView(View rootView) {
        super.onBindView(rootView);
        Logger.d("DownloadPreference: Setting title and summary to " + mTitleText + " and " + mSummaryText);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSummary = (TextView) rootView.findViewById(R.id.summary);
        mTitle.setText(mTitleText);
        mSummary.setText(mSummaryText);


    }

    @Override
    protected void onClick() {
        super.onClick();
        HTMLHelper htmlHelper = new HTMLHelper();
        if (mPackageVersionInstalled != 0) {
            htmlHelper.fetchApp((String) this.getTitle(), getContext(), 0, mKey);
        } else {
                Toast toast = Toast.makeText(getContext(),"Latest package already installed", Toast.LENGTH_SHORT);
                toast.show();

        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Add default value for the online version we'll be checking for.
        editor.putString(mKey + "_onlineVersion","0");
        editor.apply();

    }

    protected void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            PackageManager packageManager = getContext().getPackageManager();
            mPackageName = attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "package_name");
            pName = mPackageName;
            mKey = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
            mTitleText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
            mSummaryText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
            mPackageVersionInstalled = queryVersionCode(mPackageName);
            mPackageVersionName = queryVersionName(mPackageName);
            Logger.d ("DownloadPreference: Got version and name of " + mPackageVersionInstalled + " and " + mPackageVersionName);

            if (sharedPreferences.contains(mKey + "_onlineVersion")) {
                mPackageVersionAvailable = sharedPreferences.getInt(mKey + "_onlineVersion", 0);
            } else {
                mPackageVersionAvailable = 0;
            }


            if (mPackageVersionInstalled != 0) {
                if (mPackageVersionInstalled < mPackageVersionAvailable) {
                    mSummaryText = "A new version is available.  Tap to download.";
                } else {
                    mSummaryText = "Installed Version: " + mPackageVersionName;
                }
            }

        }
    }

    public void RefreshPreferenceSummary() {
        int mInstalledVer = queryVersionCode(pName);
        mPackageVersionAvailable = sharedPreferences.getInt(mKey + "_code", 0);
        Logger.d("DownloadPreference: Values for installed and online are " + mInstalledVer + " and " + mPackageVersionAvailable);
        if (mPackageVersionAvailable != 0) {
            if (mInstalledVer > mPackageVersionInstalled) {
                mSummaryText = "A new version is available.  Tap to download.";
                mSummary.setText(mSummaryText);

            } else if (mInstalledVer == mPackageVersionInstalled && !(mInstalledVer == 0)) {
                mSummaryText = "Installed Version (Latest): " + mPackageVersionName;
                mSummary.setText(mSummaryText);
            }
        }
    }

    public static String getPackageNameAttr() {
        return mPackageName;
    }


    private int queryVersionCode (String packageName) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            int versionCode = packageInfo.versionCode;
            Logger.d("DownloadPreference: version code found of " + packageInfo.versionCode);
            return versionCode;


        } catch (PackageManager.NameNotFoundException e) {

            return 0;
        }

    }

    private String queryVersionName(String packageName) {

        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String versionName = packageInfo.versionName;
            Logger.d("DownloadPreference: version Name found of " + packageInfo.versionName);
            return versionName;


        } catch (PackageManager.NameNotFoundException e) {

            return "0";
        }

    }

    public void updatOnlineVersion() {
        HTMLHelper htmlHelper = new HTMLHelper();

    }


}
