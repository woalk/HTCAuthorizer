package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String PREF_SHOW_HSP_WARN = "warn_no_hsp";
    private static String TAG = MainActivity.class.getSimpleName();
    public TextView tv1;
    public TextView tv2;
    public XMLHelper xh;
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ArrayList<NavItem> mNavItems = new ArrayList<>();
    ArrayList<Integer> mColors = new ArrayList<>();
    private String curTitle;
    private int mMainColor;
    private int mSecondaryColor;
    private int mAccentColor;
    private int curPos = 0;
    private DrawerLayout mDrawerLayout;
    private float mPosTv1;
    private float mPosTv2;
    private FragmentTransaction ft;

    public MainActivity() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_themes, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_always_active, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        //Add drawerdown items
        mNavItems.add(new NavItem("Main", R.drawable.ic_settings));
        mNavItems.add(new NavItem("Themes", R.drawable.ic_style));
        mNavItems.add(new NavItem("Reboot", R.drawable.ic_replay_white_24dp));
        mNavItems.add(new NavItem("About", R.drawable.ic_info));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        //Get colors
        xh = new XMLHelper();
        mColors = xh.readAllColors();
        mMainColor = mColors.get(0);
        mSecondaryColor = mColors.get(1);
        mAccentColor = mColors.get(2);

        // Set up initial settings for title view(s) and bar
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv1.setText("Sensify Xposed");
        tv1.setAlpha(0);
        tv2.setText(curTitle);
        tv1.setPivotX(0);
        tv2.setPivotX(0);
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(mMainColor);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);


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
                animateMenuItems(slideOffset);
            }


        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //Detect missing framework, warn if it's not there.
        maybeShowNoHSPWarn();
        //Set default fragment if initializing from first time.
        if (savedInstanceState == null) {
            fragmentSelect(curPos);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                //noinspection ConstantConditions
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    //Set fragment on resume
    public void onResume(Bundle SavedInstanceState) {
        selectItemFromDrawer(curPos);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tv2.setText(curTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.d("Mainactivity: Option ID is " + item.getItemId() + "and drawerdown state is " + mDrawerLayout.isDrawerOpen(GravityCompat.START));
        // The action bar home/up action should open or close the drawerdown.
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
    private void animateMenuItems(float slideOffset) {

        float invertPos = 1f - (slideOffset);
        mPosTv1 = slideOffset;
        mPosTv2 = invertPos;
        ObjectAnimator tv1X = ObjectAnimator.ofFloat(tv1, "scaleX", mPosTv1, slideOffset);
        ObjectAnimator tv1alpha = ObjectAnimator.ofFloat(tv1, "alpha", mPosTv1, slideOffset);
        ObjectAnimator tv2X = ObjectAnimator.ofFloat(tv2, "scaleX", mPosTv2, invertPos);
        ObjectAnimator tv2alpha = ObjectAnimator.ofFloat(tv2, "alpha", mPosTv2, invertPos);
        AnimatorSet tvset = new AnimatorSet();
        tvset.setDuration(450);
        tvset.play(tv1X).with(tv1alpha).with(tv2X).with(tv2alpha);
        tvset.start();

    }

    //Warning for missing HTC Service pack
    public void maybeShowNoHSPWarn() {
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
                                            Uri.parse("market://details?id=" + PKG_HSP)));
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
        if (position == 2) showRebootDialog();
        else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerList.setItemChecked(position, true);
            curPos = position;
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentSelect(position);
                }
            }, 100);
        }
    }

    //Self explanatory
    private void showRebootDialog() {
        TextView title = new TextView(this);
        title.setText("Would you like to reboot?");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);

        TextView msg = new TextView(this);
        msg.setText("Select an option below.");
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

    //Fragment selector
    private void fragmentSelect(int position) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        if (position == 0) {
            ft.replace(android.R.id.widget_frame, new MainPreferenceFragment());
        } else if (position == 1) {
            ft.replace(android.R.id.widget_frame, new ThemeFragment());
        } else if (position == 3) {
            ft.replace(android.R.id.widget_frame, new AboutSensifyFragment());
        }
        curTitle = mNavItems.get(curPos).mTitle;
        tv2.setText(curTitle);
        ft.commit();
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

        @SuppressLint("InflateParams")
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
            titleView.setTextColor(mAccentColor);
            iconView.setColorFilter(mAccentColor, PorterDuff.Mode.MULTIPLY);
            return view;
        }
    }
}
