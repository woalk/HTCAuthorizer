package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new
                MainPreferenceFragment()).commit();
    }
}
