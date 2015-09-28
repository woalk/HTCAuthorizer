package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class DownloadPreference extends Preference {

    private String mPackageName, mPackageVersionName, mKey, mTitleText, mSummaryText;
    private int mPackageVersionInstalled, mPackageVersionAvailable;
    private Boolean mIsInstalled;
    private SharedPreferences sharedPreferences;
    private TextView mTitle, mSummary;

    public DownloadPreference(Context context, AttributeSet attrs) {
        super(context);
        init(context, attrs);


    }

    public DownloadPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onBindView(View rootView) {
        super.onBindView(rootView);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.preference, null);
        mTitle = (TextView) v.findViewById(R.id.titleDownload);
        mSummary = (TextView) v.findViewById(R.id.summaryDownload);
        mTitle.setText(mTitleText);
        mSummary.setText(mSummaryText);

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle.setText(title);
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        mSummary.setText(summary);
    }

    @Override
    protected void onClick() {
        super.onClick();
        HTMLHelper htmlHelper = new HTMLHelper();
        if (mPackageVersionInstalled != 0) {
            htmlHelper.fetchApp((String) this.getTitle(), getContext(), 0, this);
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
            mPackageName = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "package_name");
            mKey = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
            mTitleText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
            mSummaryText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
            mPackageVersionInstalled = queryVersionCode(mPackageName);
            if (sharedPreferences.contains(mKey + "_onlineVersion")) {
                mPackageVersionAvailable = sharedPreferences.getInt(mKey + "_onlineVersion", 0);
            } else {
                mPackageVersionInstalled = 0;
            }
            if (mPackageVersionInstalled != 0) {
                if (mPackageVersionInstalled < mPackageVersionAvailable) {
                    this.setSummary("A new version is available.  Tap to download.");
                } else {
                    this.setSummary("Installed Version: " + queryVersionName(mPackageName));
                }
            }
        }
    }

    private int queryVersionCode (String packageName) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            Logger.d("DownloadFragment: version code found of " + packageInfo.versionCode);
            return packageInfo.versionCode;


        } catch (PackageManager.NameNotFoundException e) {

            return 0;
        }

    }

    private String queryVersionName(String packageName) {

        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            Logger.d("DownloadFragment: version Name found of " + packageInfo.versionName);
            return packageInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {

            return "0";
        }

    }

    public void updatOnlineVersion() {
        HTMLHelper htmlHelper = new HTMLHelper();

    }


}
