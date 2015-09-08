package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.negusoft.greenmatter.activity.MatActivity;

import java.io.IOException;


public class LogActivity extends MatActivity {
    private TextView tv1;
    private XMLHelper xh;
    private int maincolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xh = new XMLHelper();
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        try {
            maincolor = xh.readFromXML(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
