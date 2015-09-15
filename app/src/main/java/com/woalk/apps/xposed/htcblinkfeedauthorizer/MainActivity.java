package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.negusoft.greenmatter.MatPalette;
import com.negusoft.greenmatter.activity.MatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends MatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String PREF_SHOW_HSP_WARN = "warn_no_hsp";
    public static int mDefaultMainColor;
    public static int mDefaultSecondaryColor;
    public static int mDefaultAccentColor;
    public TextView tv1;
    public TextView tv2;
    public int mMainColor, mSecondaryColor, mAccentColor;
    public boolean mUseThemes;
    ListView mDrawerList;
    LinearLayout mDrawerPane, mDrawerReboot, mDrawerAbout, mMainLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private String curTitle;
    private int curPos = 0;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;


    public MainActivity() {

    }

    public MainActivity(String curTitle) {
        this.curTitle = curTitle;
    }


    public static MatPalette generateDefaultPalette() {

        return new MatPalette(mDefaultMainColor, mDefaultSecondaryColor, mDefaultAccentColor, mDefaultMainColor, mDefaultMainColor, mDefaultMainColor, mDefaultMainColor, mDefaultMainColor, mDefaultMainColor, 1.0f);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        mUseThemes = sharedPreferences.getBoolean("use_themes", false);
        mMainColor = sharedPreferences.getInt("theme_PrimaryColor", -16728577);
        mSecondaryColor = sharedPreferences.getInt("theme_PrimaryDarkColor", -16763828);
        mAccentColor = sharedPreferences.getInt("theme_AccentColor", -16728577);
        checkRomType();
        mDefaultMainColor = -16728577;
        mDefaultSecondaryColor = -16763828;
        mDefaultAccentColor = -16728577;
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_themes, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_always_active, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        //Add drawerdown items
        mNavItems.add(new NavItem("Main", R.drawable.ic_home_white_24dp));
        mNavItems.add(new NavItem("Themes", R.drawable.ic_style));
        mNavItems.add(new NavItem("Downloads", R.drawable.ic_get_app_white_24dp));
        mNavItems.add(new NavItem("Settings", R.drawable.ic_settings));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mDrawerReboot = (LinearLayout) findViewById(R.id.drawerReboot);
        mDrawerAbout = (LinearLayout) findViewById(R.id.drawerAbout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        //Get colors

        // Set up initial settings for title view(s) and bar
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        PackageManager pm = getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo("com.woalk.apps.xposed.htcblinkfeedauthorizer", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv1.setText(String.valueOf(pm.getApplicationLabel(appInfo)));
        tv1.setAlpha(0);
        tv2.setText(curTitle);
        tv1.setPivotX(0);
        tv2.setPivotX(0);

        // Set up onclick for bottom elements in drawer

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(mDrawerReboot)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    showRebootDialog();

                } else if (v.equals(mDrawerAbout)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    showAboutDialog();
                }
            }
        };
        mDrawerReboot.setOnClickListener(onClickListener);
        mDrawerAbout.setOnClickListener(onClickListener);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
                Logger.d("MainActivity: Onclick at position " + position);

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                animateMenuText(slideOffset);
            }

        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //Detect missing framework, warn if it's not there.
        maybeShowNoHSPWarn();
        //Set default fragment if initializing from first time.
        if (getIntent().hasExtra("toOpen")) {
            Bundle extras = getIntent().getExtras();
            String toOpen = extras.getString("toOpen");
            if (toOpen != null && toOpen.equals("themeFragment")) {
                curPos = 1;
                overridePalette(generateDefaultPalette());

            }
        }

        selectItemFromDrawer(curPos);
        mDrawerLayout.requestLayout();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (mUseThemes) {
                toolbar.setBackgroundColor(mMainColor);
                //overridePalette(generateDefaultPalette());
            } else {
                toolbar.setBackgroundColor(Color.rgb(Color.red(-16728577), Color.green(-16728577),
                        Color.blue(-16728577)));
                //overridePalette(generateCustomPalette());
            }

        }

    }

    @Override
    public MatPalette overridePalette(MatPalette palette) {
        //Get colors
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        mUseThemes = sharedPreferences.getBoolean("use_themes", false);
        int mTempColor = sharedPreferences.getInt("theme_PrimaryColor", 0);
        mMainColor = Color.rgb(Color.red(mTempColor), Color.green(mTempColor),
                Color.blue(mTempColor));
        mTempColor = sharedPreferences.getInt("theme_PrimaryDarkColor", 0);
        mSecondaryColor = Color.rgb(Color.red(mTempColor), Color.green(mTempColor),
                Color.blue(mTempColor));
        mTempColor = sharedPreferences.getInt("theme_AccentColor", 0);
        mAccentColor = Color.rgb(Color.red(mTempColor), Color.green(mTempColor), Color.blue(mTempColor));
        if (mUseThemes) {
            palette.setColorPrimary(mMainColor);
            palette.setColorPrimaryDark(mSecondaryColor);
            palette.setColorAccent(mAccentColor);
            palette.setColorControlNormal(mMainColor);
            palette.setColorSwitchThumbNormal(mMainColor);
            palette.setColorButtonNormal(mMainColor);
            palette.setColorControlActivated(mMainColor);
            palette.setColorControlHighlight(mMainColor);
            palette.setColorEdgeEffect(mAccentColor);
            // Load the preferences from an XML resource

        } else {
            palette.setColorPrimary(-16728577);
            palette.setColorPrimaryDark(-16763828);
            palette.setColorAccent(-16728577);
            palette.setColorControlNormal(-16728577);
            palette.setColorSwitchThumbNormal(-16728577);
            palette.setColorButtonNormal(-16728577);
            palette.setColorControlActivated(-16728577);
            palette.setColorControlHighlight(-16728577);
            palette.setColorEdgeEffect(-16728577);
        }
        return palette;
    }

    public MatPalette generateCustomPalette() {

        return new MatPalette(mMainColor, mSecondaryColor, mAccentColor, mMainColor, mMainColor, mMainColor, mMainColor, mMainColor, mMainColor, 1.0f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectItemFromDrawer(curPos);
        if (mUseThemes) {
            toolbar.setBackgroundColor(mMainColor);


        } else {
            toolbar.setBackgroundColor(Color.rgb(Color.red(-16728577), Color.green(-16728577),
                    Color.blue(-16728577)));
            overridePalette(generateCustomPalette());
        }

    }

    //Set fragment on resume

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tv2.setText(curTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //Custom animation for title text
    private void animateMenuText(float slideOffset) {

        float invertPos = 1f - (slideOffset);
        ObjectAnimator tv1X = ObjectAnimator.ofFloat(tv1, "scaleX", slideOffset, slideOffset);
        ObjectAnimator tv1alpha = ObjectAnimator.ofFloat(tv1, "alpha", slideOffset, slideOffset);
        ObjectAnimator tv2X = ObjectAnimator.ofFloat(tv2, "scaleX", invertPos, invertPos);
        ObjectAnimator tv2alpha = ObjectAnimator.ofFloat(tv2, "alpha", invertPos, invertPos);
        AnimatorSet tvset = new AnimatorSet();
        tvset.setDuration(450);
        tvset.play(tv1X).with(tv1alpha).with(tv2X).with(tv2alpha);
        tvset.start();

    }

    //Warning for missing HTC Service pack
    private void maybeShowNoHSPWarn() {
        final String PKG_HSP = "com.htc.sense.hsp";
        try {
            getPackageManager().getApplicationInfo(PKG_HSP, 0);
            return;
        } catch (PackageManager.NameNotFoundException e) {
            // not found. Proceed to install.
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.warn_no_hsp_title)
                .setMessage(R.string.warn_no_hsp_content)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=18731")));
                                } catch (android.content.ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/" +
                                                    "details?id=" + PKG_HSP)));
                                }
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor edit =
                                        getSharedPreferences("main",
                                                Context.MODE_PRIVATE).edit();
                                edit.putBoolean(PREF_SHOW_HSP_WARN, false);
                                edit.apply();
                            }
                        })
                .create().show();
    }

    //Drawer selection logic
    private void selectItemFromDrawer(final int position) {
            fragmentSelect(position);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerList.setItemChecked(position, true);
            curPos = position;


    }

    //Self explanatory
    private void showRebootDialog() {
        TextView title = new TextView(this);
        title.setText(R.string.dialog_title_reboot);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);

        TextView msg = new TextView(this);
        msg.setText(R.string.dialog_message_reboot);
        msg.setPadding(10, 10, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(15);
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setCustomTitle(title);
        builder.setView(msg);
        builder.setButton(Dialog.BUTTON_POSITIVE, "Full reboot", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{"su", "-c", "reboot now"});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "Hot reboot", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    Process proc = Runtime.getRuntime()

                            .exec(new String[]{"su", "-c", "setprop ctl.restart zygote"});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        builder.setIcon(R.drawable.ic_replay_black_24dp);
        builder.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.pref_cat_about))
                .setMessage(getString(R.string.pref_about_version_title) + Common.versionName + "\n" + getString(R.string.pref_about_dev_title) + getString(R.string.pref_about_dev_summary))
                .setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setIcon(R.drawable.ic_info);
        AlertDialog alert = builder.create();
        alert.show();


    }


    private void checkRomType() {
        Process p;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        String sense_version = "";
        String sense_sdk = "";
        try {
            p = new ProcessBuilder("/system/bin/getprop", "ro.build.sense.version").redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line=br.readLine()) != null){
                sense_version = line;
            }
            p.destroy();
            p = new ProcessBuilder("/system/bin/getprop", "ro.build.version.htcsdk").redirectErrorStream(true).start();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line2;
            while ((line2=br2.readLine()) != null){
                sense_sdk = line2;
            }

            p.destroy();
            if ((!sense_sdk.equals("")) && (!sense_version.equals(""))) {
                Logger.d("MainActivity: Sense ROM found. " + sense_sdk + " and " + sense_version);
                editor.putString("romtype","Sense");
                editor.apply();

            } else if (Build.HOST.contains("google.com") || Build.FINGERPRINT.contains("google")) {
                Logger.d("MainActivity: Google ROM found " + Build.FINGERPRINT);
                editor.putString("romtype","Google");
                editor.apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fragment selector
    private void fragmentSelect(int position) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (getIntent().hasExtra("toOpen")) {
            ft.setCustomAnimations(0, 0);

        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit);

        }
        if (position == 0) {
            ft.replace(android.R.id.widget_frame, new MainPreferenceFragment());
        } else if (position == 1) {
            ft.replace(android.R.id.widget_frame, new ThemeFragment());
        } else if (position == 2) {
            ft.replace(android.R.id.widget_frame, new DownloadFragment());
        } else if (position == 3) {
            ft.replace(android.R.id.widget_frame, new ModuleFragment());
        }
        tv2.setText(mNavItems.get(position).mTitle);
        ft.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.contains("use_themes")) {
            reResume();
        }
    }

    private void reResume() {
        Context context = this.getApplicationContext();
        Intent intent = new Intent(context, com.woalk.apps.xposed.htcblinkfeedauthorizer.MainActivity.class);
        intent.setComponent(new ComponentName("com.woalk.apps.xposed.htcblinkfeedauthorizer", "com.woalk.apps.xposed.htcblinkfeedauthorizer.MainActivity"));
        intent.putExtra("toOpen", "themeFragment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static class ThemeFragment extends PreferenceFragment {


        @Override

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_themes);

        }

        public void onResume() {
            super.onResume();

        }

    }

    public static class ThemeUpdateReceiver extends BroadcastReceiver {
        private int color1, color2, color3, color4, mixcolor;

        public ThemeUpdateReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            // This method is called when this BroadcastReceiver receives an Intent broadcast.
            Bundle extras = intent.getExtras();
            if (intent.getAction().equals("com.woalk.HTCAuthorizer.UPDATE_XML")) {

                if (extras != null) {

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (extras.containsKey("full_Array")) {

                        int[] arrayOfInt = extras.getIntArray("full_Array");
                        editor.putInt("theme_PrimaryColor", Common.lightenColor(arrayOfInt[2], .095f));
                        editor.putInt("theme_AccentColor", arrayOfInt[1]);
                        editor.putInt("theme_PrimaryDarkColor", Common.lightenColor(arrayOfInt[2],-.25f));
                        editor.putInt("theme_Comms_Primary", arrayOfInt[3]);
                        editor.putInt("theme_Comms_Light", arrayOfInt[4]);
                        editor.putInt("theme_Comms_Dark", arrayOfInt[5]);
                        editor.putInt("theme_Info_Primary", arrayOfInt[6]);
                        editor.putInt("theme_Info_Light", arrayOfInt[7]);
                        editor.putInt("theme_Info_Dark", arrayOfInt[8]);
                        editor.putInt("theme_Entertainment_Primary", arrayOfInt[9]);
                        editor.putInt("theme_Entertainment_Light", arrayOfInt[10]);
                        editor.putInt("theme_Entertainment_Dark", arrayOfInt[11]);
                        editor.apply();
                        intent = new Intent(context, MainActivity.class);
                        intent.setComponent(new ComponentName("com.woalk.apps.xposed.htcblinkfeedauthorizer", "com.woalk.apps.xposed.htcblinkfeedauthorizer.MainActivity"));
                        intent.putExtra("toOpen", "themeFragment");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            }
        }
    }

    //Custom class for our nav items
    class NavItem {
        String mTitle;

        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            } else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);
            titleView.setText(mNavItems.get(position).mTitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);
            return view;
        }
    }
}
