package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(android.R.id.title);
        setActionBar(toolbar);
        title.setText(toolbar.getTitle());
        getActionBar().setTitle(null);

        getFragmentManager().beginTransaction().replace(android.R.id.widget_frame,
                new MainPreferenceFragment()).commit();
    }
}
