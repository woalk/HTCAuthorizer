package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends ActionBarActivity {
    private static final String PREF_FILE_MAINACTIVITY = "MainActivity_pref";
    private static final String PREF_SHOW_HSP_WARN = "warn_no_hsp";
    private static String TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);

        mNavItems.add(new NavItem("General", "General app settings", R.drawable.icon_btn_previous_dark));
        mNavItems.add(new NavItem("Themes", "Theme related settings", R.drawable.icon_btn_previous_dark));
        mNavItems.add(new NavItem("Sensify", "Sensify settings and logging", R.drawable.icon_btn_previous_dark));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
        maybeShowNoHSPWarn();
        Fragment f = new MainPreferenceFragment();
        selectItemFromDrawer(0);
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
//                this,  mDrawerLayout, mToolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        );
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        mDrawerToggle.syncState();
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
        Fragment fragment = new MainPreferenceFragment();

        FragmentManager fragmentManager = getFragmentManager();

        if (position == 0) {
            getFragmentManager().beginTransaction().replace(android.R.id.widget_frame,
                    new MainPreferenceFragment()).commit();
        } else if (position == 1) {
            getFragmentManager().beginTransaction().replace(android.R.id.widget_frame,
                    new ThemeFragment()).commit();
        } else if (position == 2) {
            getFragmentManager().beginTransaction().replace(android.R.id.widget_frame,
                    new AboutSensifyFragment()).commit();
        }

        mDrawerList.setItemChecked(position, true);


        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}
