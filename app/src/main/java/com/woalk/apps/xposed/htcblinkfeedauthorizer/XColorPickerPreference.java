package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

public class XColorPickerPreference extends Preference implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private int myTheme;
    private ImageView myView;
    private LinearLayout myFrame, widgetFrame;
    private LayoutParams lParamsShow;
    private LayoutParams lParamsHide;
    private String colorname;
    private Animation mOutAnim, mInAnim;
    public static final String TAG = "XColorpickerPreference: ";
    private XMLHelper xh;
    public RelativeLayout container;
    private ObjectAnimator left, up, right, down;
    private AnimatorSet mButtonSet;
    public SeekBar hueSeekBar, satSeekBar, valueSeekBar;
    public TextView hueToolTip, satToolTip, valueToolTip;
    Window window;
    Display display;
    public int red, green, blue, hue, sat, value, seekBarLeft;
    public float[] hsv = new float[3];
    public Rect thumbRect;
    public String dave;

    public XColorPickerPreference(Context context) {
        super(context);
    }

    public XColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onBindView(View rootView) {
        super.onBindView(rootView);
        container = (RelativeLayout) rootView.findViewById(R.id.container);
        myView = (ImageView) rootView.findViewById(R.id.button);
        myFrame = (LinearLayout) rootView.findViewById(R.id.pickerframe);
        myFrame.setLayoutTransition(new LayoutTransition());
        mOutAnim = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.slideup);
        mInAnim = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.slidedown);
        myFrame.setAnimation(mOutAnim);
        myFrame.setAnimation(mInAnim);
        left = ObjectAnimator.ofFloat(myView, "x", 760, 400);
        right = ObjectAnimator.ofFloat(myView, "x", 400, 760);
        down = ObjectAnimator.ofFloat(myView, "y", 0, 750);
        up = ObjectAnimator.ofFloat(myView, "y", 750, 0);
        xh = new XMLHelper();
        myFrame.setVisibility(View.GONE);
        red = Color.red(myTheme);
        green = Color.green(myTheme);
        blue = Color.blue(myTheme);
        Color.RGBToHSV(red, green, blue, hsv);
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        Logger.d(TAG, "HSV As integers " + hue + " " + sat + " " + value);
        Logger.d(TAG + "Rootiview is "+ rootView);
        hueSeekBar = (SeekBar) rootView.findViewById(R.id.hueSeekBar);
        satSeekBar = (SeekBar) rootView.findViewById(R.id.satSeekBar);
        valueSeekBar = (SeekBar) rootView.findViewById(R.id.valueSeekBar);

        seekBarLeft = hueSeekBar.getPaddingLeft();

        hueToolTip = (TextView) rootView.findViewById(R.id.hueToolTip);
        satToolTip = (TextView) rootView.findViewById(R.id.satToolTip);
        valueToolTip = (TextView) rootView.findViewById(R.id.valueToolTip);

        hueSeekBar.setOnSeekBarChangeListener(this);
        satSeekBar.setOnSeekBarChangeListener(this);
        valueSeekBar.setOnSeekBarChangeListener(this);
        myView.setOnClickListener(this);

        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);

        //setup initial values

        Logger.d (TAG + "Colorname is " + colorname + " and " + myTheme);
        setMyColor(myTheme);
        setMyName(colorname);
        container.setOnClickListener(this);


    }

    public void updateBars() {
        valueSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.MULTIPLY);
        valueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        valueToolTip.setTextColor(Color.HSVToColor(hsv));
        satSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.MULTIPLY);
        satSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        satToolTip.setTextColor(Color.HSVToColor(hsv));
        hueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        hueToolTip.setTextColor(Color.HSVToColor(hsv));
    }

    public void toggle_contents() {


        if( myFrame.isShown() )
        {
          AnimatorSet mButtonSet1 = new AnimatorSet();
            mButtonSet1.setDuration(500);
            mButtonSet1.setInterpolator(new AccelerateDecelerateInterpolator());

            mButtonSet1.play(up).with(right);
            mButtonSet1.start();
            myFrame.setVisibility(View.GONE);
        }
        else if ( !myFrame.isShown() )
        {

            myFrame.setVisibility(LinearLayout.VISIBLE);
            AnimatorSet mButtonSet2 = new AnimatorSet();
            mButtonSet2.setDuration(500);
            mButtonSet2.setInterpolator(new AccelerateDecelerateInterpolator());

            Animation animation2   =    AnimationUtils.loadAnimation(getContext(), R.anim.drawer);
            animation2.setDuration(500);
            myFrame.setAnimation(animation2);
            myFrame.animate();
            mButtonSet2.play(down).with(left);
            animation2.start();
            mButtonSet2.start();

        }

    }

    public void setMyColor(int themecolor) {
        myTheme = themecolor;

        if (myView != null) {
            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(10);
            sd.setIntrinsicWidth(10);
            sd.getPaint().setColor(myTheme);

            //window.setStatusBarColor(myTheme);

            myView.setBackground(sd);
        }
    }

    public void setMyName(String name) {
        colorname = name;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        setToolTips();
    }

    public void setToolTips() {
        thumbRect = hueSeekBar.getThumb().getBounds();

        hueToolTip.setX(seekBarLeft + thumbRect.left);
        if (hue < 10)
            hueToolTip.setText("  " + hue);
        else if (hue < 100)
            hueToolTip.setText(" " + hue);
        else
            hueToolTip.setText(hue + "");

        thumbRect = satSeekBar.getThumb().getBounds();

        satToolTip.setX(seekBarLeft + thumbRect.left);
        if (sat < 10)
            satToolTip.setText("  " + sat);
        else if (sat < 100)
            satToolTip.setText(" " + sat);
        else
            satToolTip.setText(sat + "");

        thumbRect = valueSeekBar.getThumb().getBounds();

        valueToolTip.setX(seekBarLeft + thumbRect.left);
        if (value < 10)
            valueToolTip.setText("  " + value);
        else if (value < 100)
            valueToolTip.setText(" " + value);
        else
            valueToolTip.setText(value + "");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.hueSeekBar) {

            hue = progress;
            hsv[0] = (float) hue;

        } else if (seekBar.getId() == R.id.satSeekBar) {

            sat = progress;
            hsv[1] = (float) sat / 100;

        } else if (seekBar.getId() == R.id.valueSeekBar) {

            value = progress;
            hsv[2] = (float) value / 100;

        }
        Logger.d(TAG, "HSV Components are " + hsv[0] + " " + hsv[1] + " " + hsv[2]);
        setToolTips();
        setMyColor(Color.HSVToColor(hsv));
        updateBars();

    }


    public int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public void saveMyColor(String colorname, int myTheme) {
        xh.WriteToXML(colorname, myTheme);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (v == myView) {
            Logger.d(TAG + "Click detected, outputting color of " + hsv);
            saveMyColor(colorname, Color.HSVToColor(hsv));
            toggle_contents();
        } else if (v == container) {

                toggle_contents();


        }
    }
}
