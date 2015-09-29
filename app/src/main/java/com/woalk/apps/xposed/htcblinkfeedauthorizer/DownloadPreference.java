package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.Locale;


public class DownloadPreference extends Preference {

    public static String mPackageName;
    private String pName;
    private String mPackageVersionName;
    private String mKey;
    private String mTitleText;
    private String mSummaryText;
    private int mPackageVersionInstalled, mPackageVersionAvailable;
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
    public String getKey() { return mKey; }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mTitle = (TextView) view.findViewById(R.id.title);
        mSummary = (TextView) view.findViewById(R.id.summary);
        mTitle.setText(mTitleText);
        mSummary.setText(mSummaryText);
        RefreshPreferenceSummary();


    }

    @Override
    protected void onClick() {
        super.onClick();
        HTMLHelper htmlHelper = new HTMLHelper();
        HTMLHelper htmlHelper1 = new HTMLHelper();
        if ((mPackageVersionInstalled == 0) || ((mPackageVersionInstalled < mPackageVersionAvailable) && mPackageVersionInstalled != 0 )) {
            htmlHelper.fetchApp((String) this.getTitle(), getContext(), 1, mKey);
            Locale current = getContext().getResources().getConfiguration().locale;
            Logger.d("DownloadFragment: Locale is " + current);
            if (mKey.equals("dl_Prism")) {
                htmlHelper1.fetchApp((String) "HTC Account", getContext(), 1, mKey);
            } else if (mKey.equals("dl_IME")) {
                if (current.toString().equals("en_US")) {
                    htmlHelper1.fetchApp("Keyboard - English Pack with ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("es_US")) {
                    htmlHelper1.fetchApp("Keyboard Spanish ALM", getContext(), 0, mKey);
                } else if (current.toString().contains("en_")) {
                    htmlHelper1.fetchApp("Keyboard English_UK ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("lv_LV")) {
                    htmlHelper1.fetchApp("Keyboard Latvian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("lt_LT")) {
                    htmlHelper1.fetchApp("Keyboard Lithuanian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("nb_NO")) {
                    htmlHelper1.fetchApp("Keyboard Norwegian ALM", getContext(), 0, mKey);
                } else if ((current.toString().equals("it_IT")) || (current.toString().equals("it_CH"))) {
                    htmlHelper1.fetchApp("Keyboard Italian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("ru_RU")) {
                    htmlHelper1.fetchApp("Keyboard Russian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("uk_UA")) {
                    htmlHelper1.fetchApp("Keyboard Ukranian ALM", getContext(), 0, mKey);
                } else if (current.toString().contains("de_")) {
                    htmlHelper1.fetchApp("Keyboard German ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("cs_CZ")) {
                    htmlHelper1.fetchApp("Keyboard Czech ALM", getContext(), 0, mKey);
                } else if ((current.toString().equals("nl_BE")) || (current.toString().equals("nl_NL"))) {
                    htmlHelper1.fetchApp("Keyboard Dutch ALM", getContext(), 0, mKey);
                } else if (current.toString().contains("fr_")) {
                    htmlHelper1.fetchApp("Keyboard French ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("pl_PL")) {
                    htmlHelper1.fetchApp("Keyboard Polish ALM", getContext(), 0, mKey);
                } else if (current.toString().contains("ar_")) {
                    htmlHelper1.fetchApp("Keyboard Arabic ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("es_ES")) {
                    htmlHelper1.fetchApp("Keyboard Polish ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("bg_BG")) {
                    htmlHelper1.fetchApp("Keyboard Bulgarian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("ca_ES")) {
                    htmlHelper1.fetchApp("Keyboard Catalan ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("hr_HR")) {
                    htmlHelper1.fetchApp("Keyboard Croatian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("da_DK")) {
                    htmlHelper1.fetchApp("Keyboard Danish ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("el_GR")) {
                    htmlHelper1.fetchApp("Keyboard Greek ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("iw_IL")) {
                    htmlHelper1.fetchApp("Keyboard Hebrew ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("in_ID")) {
                    htmlHelper1.fetchApp("Keyboard Indonesian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("bg_BG")) {
                    htmlHelper1.fetchApp("Keyboard Bulgarian ALM", getContext(), 0, mKey);
                } else if (current.toString().contains("pt_")) {
                    htmlHelper1.fetchApp("Keyboard Portuguese ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("ro_RO")) {
                    htmlHelper1.fetchApp("Keyboard Romanian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("sr_RS")) {
                    htmlHelper1.fetchApp("Keyboard Serbian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("sk_SK")) {
                    htmlHelper1.fetchApp("Keyboard Slovakian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("sl_SI")) {
                    htmlHelper1.fetchApp("Keyboard Slovenian ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("sv_SE")) {
                    htmlHelper1.fetchApp("Keyboard Swedish ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("th_TH")) {
                    htmlHelper1.fetchApp("Keyboard Thai ALM", getContext(), 0, mKey);
                } else if (current.toString().equals("tr_RT")) {
                    htmlHelper1.fetchApp("Keyboard Turkish ALM", getContext(), 0, mKey);
                }
            }
        } else {

            Toast toast = Toast.makeText(getContext(), "Latest package already installed", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Add default value for the online version we'll be checking for.
        editor.putInt(mKey + "_code", 0);
        editor.apply();

    }

    protected void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mPackageName = attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "package_name");
            pName = mPackageName;
            mKey = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
            mTitleText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
            mSummaryText = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
            mPackageVersionInstalled = queryVersionCode(mPackageName);
            mPackageVersionName = queryVersionName(mPackageName);
            mPackageVersionAvailable = sharedPreferences.getInt(mKey + "_code", 0);

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals("com.woalk.HTCAuthorizer.VERSION_FETCHED")) {
                        mPackageVersionAvailable = intent.getIntExtra("version_code",0);
                        Logger.d("DownloadPreference: Recevied extra of " + mPackageVersionAvailable);
                        RefreshPreferenceSummary();

                    }

                }
            };

            context.registerReceiver(receiver, new IntentFilter(
                    "com.woalk.HTCAuthorizer.VERSION_FETCHED"));

        }
    }

    public void RefreshPreferenceSummary() {
        int mInstalledVer = queryVersionCode(pName);
        String mKeyCode = mKey + "_code";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPackageVersionAvailable = sharedPreferences.getInt(mKey + "_code", 0);
        Logger.d("DownloadPreference: a summary refresh was triggered for package " + pName + " and " + mKeyCode + " with installed of " + mInstalledVer + " and available of " + mPackageVersionAvailable);
        if (mPackageVersionAvailable != 0) {
            if (mInstalledVer < mPackageVersionAvailable && !(mInstalledVer == 0)) {
                mSummaryText = "A new version is available.  Tap to download.";
                mSummary.setText(mSummaryText);

            } else if (mInstalledVer == mPackageVersionAvailable && !(mInstalledVer == 0)) {
                mSummaryText = "Installed Version (Latest): " + mPackageVersionName;
                mSummary.setText(mSummaryText);
            }
        }
    }

    private int queryVersionCode(String packageName) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;


        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }

    }

    private String queryVersionName(String packageName) {

        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            return "0";
        }

    }

}
