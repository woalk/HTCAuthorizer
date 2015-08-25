package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_FILE_MAINACTIVITY = "MainActivity_pref";
    private static final String PREF_SHOW_HSP_WARN = "warn_no_hsp";
    private static String TAG = MainActivity.class.getSimpleName();
    public TextSwitcher textSwitcher;
    public TextView tv1;
    public TextView tv2;
    public XMLHelper xh;
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ArrayList<NavItem> mNavItems = new ArrayList<>();
    private int curPos = 0;
    private DrawerLayout mDrawerLayout;
    private int mAccentColor;
    private float mPosTv1;
    private float mPosTv2;
    private ValueAnimator anim;

    public MainActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);
        mNavItems.add(new NavItem("Main", "Primary Sensify settings", R.drawable.ic_settings));
        mNavItems.add(new NavItem("Themes", "Theme related settings", R.drawable.ic_style));
        mNavItems.add(new NavItem("About", "Always-on features, app logs, module info.", R.drawable.ic_info));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        xh = new XMLHelper();
        try {
            mAccentColor = xh.readFromXML(2);
        } catch (IOException e) {
            Logger.e(TAG + " Error reading xml");
        }

        // Set up initial settings for title view(s)
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv1.setText("Sensify Xposed");
        tv1.setAlpha(0);
        tv2.setText(mNavItems.get(curPos).mTitle);
        tv1.setPivotX(0);
        tv2.setPivotX(0);


        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
                curPos = position;
            }
        });

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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

            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {



                } else if (newState == DrawerLayout.STATE_DRAGGING && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {


                }
            }
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                animateMenuItems(slideOffset);
            }




        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBarDrawerToggle.syncState();
        maybeShowNoHSPWarn();
        selectItemFromDrawer(0);
    }


    private void animateMenuItems(float slideOffset) {

        ObjectAnimator tv1X = ObjectAnimator.ofFloat(tv1, "scaleX", mPosTv1, slideOffset);
        ObjectAnimator tv1alpha = ObjectAnimator.ofFloat(tv1, "alpha", mPosTv1, slideOffset);
        AnimatorSet tv1set = new AnimatorSet();
        tv1set.play(tv1X).with(tv1alpha);
        tv1set.setDuration(450);

        float invertPos = (float) (1f - (slideOffset));
        ObjectAnimator tv2X = ObjectAnimator.ofFloat(tv2, "scaleX", mPosTv2, invertPos);
        ObjectAnimator tv2alpha = ObjectAnimator.ofFloat(tv2, "alpha", mPosTv2, invertPos);
        AnimatorSet tv2set = new AnimatorSet();
        tv2set.play(tv2X).with(tv2alpha);
        tv2set.setDuration(450);
        tv1set.start();
        tv2set.start();

        mPosTv1 = slideOffset;
        mPosTv2 = invertPos;


    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.d("Mainactivity: Option ID is " + item.getItemId() + "and drawer state is " + mDrawerLayout.isDrawerOpen(GravityCompat.START));
        // The action bar home/up action should open or close the drawer.
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
                                        getSharedPreferences(PREF_FILE_MAINACTIVITY,
                                                Context.MODE_PRIVATE).edit();
                                edit.putBoolean(PREF_SHOW_HSP_WARN, false);
                                edit.apply();
                            }
                        })
                .create().show();
    }

    private void selectItemFromDrawer(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        if (position == 0) {
            ft.replace(android.R.id.widget_frame, new MainPreferenceFragment());
        } else if (position == 1) {
            ft.replace(android.R.id.widget_frame, new ThemeFragment());
        } else if (position == 2) {
            ft.replace(android.R.id.widget_frame, new AboutSensifyFragment());
        }


        mDrawerList.setItemChecked(position, true);

        setTitle("");

        // Close the drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        ft.commit();


    }


    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
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
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(position).mTitle);
            subtitleView.setText(mNavItems.get(position).mSubtitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);
            titleView.setTextColor(mAccentColor);
            subtitleView.setTextColor(mAccentColor);
            iconView.setColorFilter(mAccentColor, PorterDuff.Mode.MULTIPLY);

            return view;
        }
    }
}
