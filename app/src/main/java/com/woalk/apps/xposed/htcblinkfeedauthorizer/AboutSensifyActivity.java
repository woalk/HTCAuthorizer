package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AboutSensifyActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pref);

        setActionBar((android.widget.Toolbar) findViewById(R.id.toolbar));
        //noinspection ConstantConditions
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.widget_frame,

                new AboutSensifyFragment()).commit();


    }


}