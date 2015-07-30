package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);

        setActionBar((Toolbar) findViewById(R.id.toolbar));
        //noinspection ConstantConditions
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.activity_log, (ViewGroup) findViewById(android.R.id.widget_frame), true);

        final WebView v = (WebView) findViewById(R.id.log);
        v.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        v.getSettings().setUseWideViewPort(true);
        Logger.readLogcat(v);

    }
}
