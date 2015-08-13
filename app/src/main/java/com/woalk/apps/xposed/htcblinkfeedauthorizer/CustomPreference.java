package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CustomPreference extends Preference {
    private int myTheme;
    private ImageView myView;

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
        myView = (ImageView) rootView.findViewById(R.id.button);
        setMyColor(myTheme);
    }

    public void setMyColor(int themecolor) {
        myTheme = themecolor;
        Logger.d("CustomPreference: Color passed " + myTheme);
        if (myView != null) {
            Logger.d("CustomPreference: It works!" + " " + myView + " and color of " + myTheme);
            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(10);
            sd.setIntrinsicWidth(10);
            sd.getPaint().setColor(myTheme);
            myView.setBackground(sd);
        } else {
            Logger.d("CustomPreference: Close, but no cigar!" + " " + myView + " and color of " + myTheme);

        }
    }
}
