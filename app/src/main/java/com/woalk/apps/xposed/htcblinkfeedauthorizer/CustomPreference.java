package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CustomPreference extends Preference {
    public Integer myTheme = null;

    public CustomPreference(Context context) {
        super(context);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onBindView(View rootView) {
        super.onBindView(rootView);
        ImageView myView = (ImageView) rootView.findViewById(R.id.button);

        if ((myView != null) && (myTheme != null)) {
            Logger.d("CustomPreference: It works!" + " " + myView + " and color of " + myTheme);
            myView.setBackgroundColor(myTheme);
        } else {
            Logger.d("CustomPreference: Close, but no cigar!" + " " + myView + " and color of " + myTheme);
        }

    }


    public void setMyColor(int themecolor) {
        myTheme = themecolor;
        Logger.d("CustomPreference: Color passed " + myTheme);
    }

}
