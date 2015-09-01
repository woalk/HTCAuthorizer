package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class XColorPickerPreference extends Preference implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    public static final String TAG = "XColorpickerPreference: ";
    public static int SPEED_ANIMATION_TRANSITION = 500;
    public RelativeLayout container;
    public com.woalk.apps.xposed.htcblinkfeedauthorizer.AnimatingRelativeLayout pickerFrame;
    public SeekBar hueSeekBar, satSeekBar, valueSeekBar;
    public TextView hueToolTip, satToolTip, valueToolTip;
    public int red, green, blue, hue, sat, value, purehue, mPickerBottom;
    public float[] hsv = new float[3];
    public float[] hsvsat = new float[3];
    public float[] hsvvalue = new float[3];
    public Rect hueRect, satRect, valueRect;
    private int myTheme, mScreenWidth;
    private float buttonX, buttonY;
    private ImageView pickerButton;
    private String colorname;
    private AnimatorSet mButtonHideSet, mButtonShowSet;
    private XMLHelper xh;
    private ObjectAnimator left, up, right, down;
    private ColorMatrix matrixHue, matrixSat;

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

        //grab screen elements
        container = (RelativeLayout) rootView.findViewById(R.id.container);
        pickerButton = (ImageView) rootView.findViewById(R.id.button);
        pickerFrame = (com.woalk.apps.xposed.htcblinkfeedauthorizer.AnimatingRelativeLayout) rootView.findViewById(R.id.pickerframe);

        // read positions of button, container height, screen width
        buttonX = pickerButton.getX();
        buttonY = pickerButton.getY();
        mPickerBottom = pickerButton.getBottom();
        mScreenWidth = ((getContext().getResources().getDisplayMetrics().widthPixels / 2) - 110);

        //set up animations
        right = ObjectAnimator.ofFloat(pickerButton, "translationX", buttonX);
        left = ObjectAnimator.ofFloat(pickerButton, "translationX", -(mScreenWidth));
        down = ObjectAnimator.ofFloat(pickerButton, "translationY", 350);
        up = ObjectAnimator.ofFloat(pickerButton, "translationY", buttonY);

        mButtonHideSet = new AnimatorSet();
        mButtonShowSet = new AnimatorSet();
        mButtonHideSet.setDuration(SPEED_ANIMATION_TRANSITION);
        mButtonShowSet.setDuration(SPEED_ANIMATION_TRANSITION);
        mButtonHideSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.play(down).with(left);
        mButtonHideSet.play(up).with(right);
        matrixHue = new ColorMatrix();
        matrixSat = new ColorMatrix();

        xh = new XMLHelper();
        red = Color.red(myTheme);
        green = Color.green(myTheme);
        blue = Color.blue(myTheme);

        hsv[0] = hue;
        hsv[1] = sat;
        hsv[2] = value;
        matrixHue.setSaturation(hsv[1]);


        Color.RGBToHSV(red, green, blue, hsv);
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        hsvsat[0] = hsv[0];
        hsvsat[1] = 0.75f;
        hsvsat[2] = hsv[2];
        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvvalue[2] = 0.75f;
        Logger.d(TAG + " HSV As integers " + hue + " " + sat + " " + value);
        Logger.d(TAG + "Rootiview is " + rootView);
        hueSeekBar = (SeekBar) rootView.findViewById(R.id.hueSeekBar);
        satSeekBar = (SeekBar) rootView.findViewById(R.id.satSeekBar);
        valueSeekBar = (SeekBar) rootView.findViewById(R.id.valueSeekBar);

        hueToolTip = (TextView) rootView.findViewById(R.id.hueToolTip);
        satToolTip = (TextView) rootView.findViewById(R.id.satToolTip);
        valueToolTip = (TextView) rootView.findViewById(R.id.valueToolTip);

        hueSeekBar.setOnSeekBarChangeListener(this);
        satSeekBar.setOnSeekBarChangeListener(this);
        valueSeekBar.setOnSeekBarChangeListener(this);
        pickerButton.setOnClickListener(this);

        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);
        pickerFrame.hide(true);

        //setup initial values

        Logger.d(TAG + "Colorname is " + colorname + " and " + myTheme);
        setMyColor(myTheme);
        setMyName(colorname);
        container.setOnClickListener(this);


    }

    public void updateSliders() {
        //recalculate individual values
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvsat[0] = hsv[0];
        hsvsat[2] = hsv[2];
        matrixHue.setSaturation(hsv[1]);

        matrixSat.set(new float[]{1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0});
        ColorMatrix colorFilterMatrix = new ColorMatrix();
        colorFilterMatrix.postConcat(matrixHue);
        colorFilterMatrix.postConcat(matrixSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorFilterMatrix);

        hueSeekBar.getProgressDrawable().setColorFilter(filter);
        satSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsvsat), PorterDuff.Mode.MULTIPLY);
        valueSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsvvalue), PorterDuff.Mode.MULTIPLY);

        hueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        satSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        valueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);

        hueToolTip.setTextColor(Color.HSVToColor(hsv));
        satToolTip.setTextColor(Color.HSVToColor(hsv));
        valueToolTip.setTextColor(Color.HSVToColor(hsv));

        hueRect = hueSeekBar.getThumb().getBounds();
        satRect = satSeekBar.getThumb().getBounds();
        valueRect = valueSeekBar.getThumb().getBounds();
        Logger.d(TAG + "Bounds are" + valueRect);
        hueToolTip.setX(16 + hueRect.left);
        satToolTip.setX(16 + satRect.left);
        valueToolTip.setX(16 + valueRect.left);

        if (hue < 10)
            hueToolTip.setText("  " + hue);
        else if (hue < 100)
            hueToolTip.setText(" " + hue);
        else
            hueToolTip.setText(hue + "");

        if (sat < 10)
            satToolTip.setText("  " + sat);
        else if (sat < 100)
            satToolTip.setText(" " + sat);
        else
            satToolTip.setText(sat + "");


        if (value < 10)
            valueToolTip.setText("  " + value);
        else if (value < 100)
            valueToolTip.setText(" " + value);
        else
            valueToolTip.setText(value + "");
    }

    public void toggle_contents() {


        if (pickerFrame.isShown()) {

            mButtonHideSet.start();
            pickerFrame.hide(true);


        } else if (!pickerFrame.isShown()) {

            pickerFrame.show(true);
            pickerFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSliders();
                }
            }, 100);


            mButtonShowSet.start();

        }

    }

    //setter for Picker button color
    public void setMyColor(int themecolor) {
        myTheme = themecolor;

        if (pickerButton != null) {
            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(10);
            sd.setIntrinsicWidth(10);
            sd.getPaint().setColor(myTheme);

            pickerButton.setBackground(sd);
        }
    }


    public void setMyName(String name) {
        colorname = name;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

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
        setMyColor(Color.HSVToColor(hsv));
        updateSliders();

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
        if (v == pickerButton) {
            Logger.d(TAG + "Click detected, outputting color of " + hsv);
            saveMyColor(colorname, Color.HSVToColor(hsv));
            toggle_contents();
        } else if (v == container) {

            toggle_contents();


        }
    }
}
