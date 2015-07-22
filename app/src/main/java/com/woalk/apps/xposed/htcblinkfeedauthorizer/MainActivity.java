package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends Activity {
    private static final String PREF_FILE_MAINACTIVITY = "MainActivity_pref";
    private static final String PREF_SHOW_HSP_WARN = "warn_no_hsp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(android.R.id.title);
        setActionBar(toolbar);
        title.setText(toolbar.getTitle());
        //noinspection ConstantConditions
        getActionBar().setTitle(null);

        Fragment f = new MainPreferenceFragment();

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(MainPreferenceFragment.EXTRA_SUBSCREEN_ID)) {
            Bundle args = new Bundle();
            args.putInt(MainPreferenceFragment.EXTRA_SUBSCREEN_ID,
                    getIntent().getExtras().getInt(MainPreferenceFragment.EXTRA_SUBSCREEN_ID));
            f.setArguments(args);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            title.setText(title.getText() + " - " +
                    getString(R.string.pref_general_always_active_window));
        } else if (getSharedPreferences(PREF_FILE_MAINACTIVITY, Context.MODE_PRIVATE)
                    .getBoolean(PREF_SHOW_HSP_WARN, true)) {
            maybeShowNoHSPWarn();
        }

        getFragmentManager().beginTransaction().replace(android.R.id.widget_frame, f).commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
