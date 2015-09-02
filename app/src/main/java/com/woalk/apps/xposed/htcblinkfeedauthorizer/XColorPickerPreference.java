package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class XColorPickerPreference extends Preference implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    public static int SPEED_ANIMATION_TRANSITION = 400;
    public RelativeLayout container;
    public RelativeLayout pickerFrame;
    public SeekBar hueSeekBar, satSeekBar, valueSeekBar;
    public TextView hueToolTip, satToolTip, valueToolTip;
    public int hue, sat, value, red, green, blue, original;
    public int mPickerBottom;
    public float[] hsv = new float[3];
    public float[] hsv_orig = new float[3];
    public float[] hsvsat = new float[3];
    public float[] hsvvalue = new float[3];
    private int myTheme;
    private ImageButton pickerButton;
    private String colorname;
    private AnimatorSet mButtonHideSet = new AnimatorSet(), mButtonShowSet = new AnimatorSet();
    private AlphaAnimation mPickerPanelShow, mPickerPanelHide;
    private XMLHelper xh;
    private ColorMatrix matrixValue, matrixSat;

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

        xh = new XMLHelper();

        //grab screen elements
        container = (RelativeLayout) rootView.findViewById(R.id.container);
        pickerButton = (ImageButton) rootView.findViewById(R.id.button);
        pickerFrame = (RelativeLayout) rootView.findViewById(R.id.pickerframe);

        // read positions of button, container height, screen width
        float buttonX = pickerButton.getX();
        float buttonY = pickerButton.getY();
        mPickerBottom = pickerButton.getBottom();
        int mScreenWidth = ((getContext().getResources().getDisplayMetrics().widthPixels / 2) - 120);

        //set up animations
        ObjectAnimator right = ObjectAnimator.ofFloat(pickerButton, "translationX", buttonX);
        ObjectAnimator left = ObjectAnimator.ofFloat(pickerButton, "translationX", -(mScreenWidth));
        ObjectAnimator down = ObjectAnimator.ofFloat(pickerButton, "translationY", 350);
        ObjectAnimator up = ObjectAnimator.ofFloat(pickerButton, "translationY", buttonY);

        //build sets of animations for button
        mButtonHideSet.setDuration(SPEED_ANIMATION_TRANSITION + 350);
        mButtonShowSet.setDuration(SPEED_ANIMATION_TRANSITION + 350);
        mButtonHideSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.play(down).with(left);
        mButtonHideSet.play(up).with(right);

        mPickerPanelShow = new AlphaAnimation(0.0f, 1.0f);
        mPickerPanelShow.setDuration(SPEED_ANIMATION_TRANSITION);
        mPickerPanelShow.setFillAfter(true);

        mPickerPanelHide = new AlphaAnimation(1.0f, 0.0f);
        mPickerPanelHide.setDuration(SPEED_ANIMATION_TRANSITION);
        mPickerPanelHide.setFillAfter(true);

        matrixValue = new ColorMatrix();
        matrixSat = new ColorMatrix();

        hsv = intToHSV(myTheme);
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);

        hsvsat[0] = hsv[0];
        hsvsat[1] = 0.8f;
        hsvsat[2] = hsv[2];

        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvvalue[2] = 0.8f;

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
        container.setOnClickListener(this);

        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);

        pickerFrame.setVisibility(View.GONE);

        setMyColor(myTheme);
        original = myTheme;

        setMyName(colorname);

    }

    public void updateSliders() {
        pickerFrame.requestLayout();
        //recalculate individual values
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);

        //set up arrays for coloring hue and sat bars
        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvsat[0] = hsv[0];
        hsvsat[2] = hsv[2];

        //convert values into filters
        matrixSat.setSaturation(hsv[1]);
        ColorMatrix matrixSatValue = new ColorMatrix();
        matrixSatValue.setConcat(setContrast(hsv[2]), matrixSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixSatValue);

        //set filters on each seek bar
        hueSeekBar.getProgressDrawable().setColorFilter(filter);
        satSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsvsat), PorterDuff.Mode.MULTIPLY);
        valueSeekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(hsvvalue), PorterDuff.Mode.MULTIPLY);

        //color thumb and tooltip text
        hueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        satSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        valueSeekBar.getThumb().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
        hueToolTip.setTextColor(Color.HSVToColor(hsv));
        satToolTip.setTextColor(Color.HSVToColor(hsv));
        valueToolTip.setTextColor(Color.HSVToColor(hsv));

        //center tooltips over thumb
        hueToolTip.setX(hueSeekBar.getThumb().getBounds().left);
        satToolTip.setX(satSeekBar.getThumb().getBounds().left);
        valueToolTip.setX(valueSeekBar.getThumb().getBounds().left);

        //set text to current value
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

    public float[] intToHSV(int inColor) {
        red = Color.red(myTheme);
        green = Color.green(myTheme);
        blue = Color.blue(myTheme);
        float hsv[] = new float[3];

        Color.RGBToHSV(red, green, blue, hsv);
        return hsv;
    }

    ColorMatrix setContrast(float contrast) {
        float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        float[] array = new float[] {
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0};
        ColorMatrix matrix = new ColorMatrix(array);
        return matrix;
    }

    public void toggle_contents() {

        if (pickerFrame.isShown()) {

            mButtonHideSet.start();
            pickerButton.setImageResource(0);
            setMyColor(original);
            hsv=intToHSV(original);
            ExpandAnimation expandAni = new ExpandAnimation(pickerFrame, SPEED_ANIMATION_TRANSITION);
            Animation startanim = AnimationUtils.loadAnimation(this.getContext(),R.anim.drawerup);

            pickerFrame.startAnimation(expandAni);

        } else if (!pickerFrame.isShown()) {

            ExpandAnimation expandAni = new ExpandAnimation(pickerFrame, SPEED_ANIMATION_TRANSITION);
            Animation endanim = AnimationUtils.loadAnimation(this.getContext(),R.anim.drawerdown);

            pickerFrame.startAnimation(expandAni);
            //run in a postdelayed gives it time to update
            pickerFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSliders();
                    pickerButton.setImageResource(R.drawable.ic_add_white_24dp);

                }
            }, 300);

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

        setMyColor(Color.HSVToColor(hsv));
        updateSliders();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void saveMyColor(String colorname, int myTheme) {

        xh.WriteToXML(colorname, myTheme);


    }

    @Override
    public void onClick(View v) {
        if (v == pickerButton) {
            saveMyColor(colorname, Color.HSVToColor(hsv));
            original = Color.HSVToColor(hsv);
            toggle_contents();
        } else if (v == container) {

            toggle_contents();
        }
    }
}
