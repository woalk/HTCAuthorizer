package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.negusoft.greenmatter.activity.MatActivity;


public class LogActivity extends MatActivity {
    private TextView tv1;
    private int maincolor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        maincolor = sharedPreferences.getInt("theme_PrimaryColor",0);;
        toolbar.setBackgroundColor(maincolor);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("Logging");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.activity_log, (ViewGroup) findViewById(android.R.id.widget_frame), true);

        final WebView v = (WebView) findViewById(R.id.log);
        v.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        v.getSettings().setUseWideViewPort(true);
        Logger.readLogcat(v);

    }
}
