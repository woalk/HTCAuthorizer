package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

public class CustomPreference extends Preference {
    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindView(View rootView) {
        super.onBindView(rootView);

        View myView = rootView.findViewById(R.id.button);
        //Stop here for the night.
        //myView.getDrawable().setColorFilter();
        Logger.d("CustomPreference: It works!" + myView);
        // do something with myView
    }
}
