package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class LogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);
        setTitle("Logging");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);


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
