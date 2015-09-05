package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class XColorPickerPreference extends Preference implements View.OnClickListener, DiscreteSeekBar.onSeekBarChangeListener {
    public static int SPEED_ANIMATION_TRANSITION = 500;
    public RelativeLayout container;
    public RelativeLayout pickerFrame;
    public org.adw.library.widgets.discreteseekbar.DiscreteSeekBar hueSeekBar, satSeekBar, valueSeekBar;
    public TextView hueToolTip, satToolTip, valueToolTip;
    public int hue, sat, value, red, green, blue, original;
    public int mPickerBottom;
    public float[] hsv = new float[3];
    public float[] hsv_orig = new float[3];
    public float[] hsvsat = new float[3];
    public float[] hsvvalue = new float[3];
    private int myTheme;
    private ImageButton pickerButton;
    private Drawable btnIconDrawable;
    private String colorname;
    private AnimatorSet mButtonHideSet = new AnimatorSet(), mButtonShowSet = new AnimatorSet();
    private AlphaAnimation mPickerPanelShow, mPickerPanelHide;
    private ObjectAnimator down, left, alphaIn, alphaOut, raise, lower;
    private XMLHelper xh;
    private boolean isAnimating, isPickerFrameShowing;
    private ColorMatrix matrixValue, matrixSat;
    private android.support.v7.widget.Toolbar toolbar;

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
        pickerButton.setImageResource(R.drawable.ic_add_white_24dp);
        btnIconDrawable = pickerButton.getDrawable();
        btnIconDrawable.setAlpha(0);
        isPickerFrameShowing = false;

        // read positions of button, container height, screen width
        float buttonX = pickerButton.getX();
        float buttonY = pickerButton.getY();
        mPickerBottom = pickerButton.getBottom();
        int mScreenWidth = ((getContext().getResources().getDisplayMetrics().widthPixels / 2) - 120);
        alphaIn = ObjectAnimator.ofInt(btnIconDrawable, "alpha", 0, 255);
        alphaOut = ObjectAnimator.ofInt(btnIconDrawable, "alpha", 255, 0);
        alphaIn.setDuration(SPEED_ANIMATION_TRANSITION);
        //set up animations
        ObjectAnimator right = ObjectAnimator.ofFloat(pickerButton, "translationX", buttonX);
        raise = ObjectAnimator.ofFloat(pickerButton, "elevation", 0.0f, 10.0f);
        lower = ObjectAnimator.ofFloat(pickerButton, "elevation", 10.0f, 0.0f);
        left = ObjectAnimator.ofFloat(pickerButton, "translationX", -(mScreenWidth));
        down = ObjectAnimator.ofFloat(pickerButton, "translationY", 350);
        ObjectAnimator up = ObjectAnimator.ofFloat(pickerButton, "translationY", buttonY);

        //build sets of animations for button
        mButtonHideSet.setDuration(SPEED_ANIMATION_TRANSITION);
        mButtonShowSet.setDuration(SPEED_ANIMATION_TRANSITION);
        mButtonHideSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mButtonShowSet.play(down).with(left).with(alphaIn).with(raise);
        mButtonHideSet.play(up).with(right).with(alphaOut).with(lower);

        mPickerPanelShow = new AlphaAnimation(0.0f, 1.0f);
        mPickerPanelShow.setDuration(SPEED_ANIMATION_TRANSITION);
        mPickerPanelShow.setFillAfter(true);

        mPickerPanelHide = new AlphaAnimation(1.0f, 0.0f);
        mPickerPanelHide.setDuration(SPEED_ANIMATION_TRANSITION);
        mPickerPanelHide.setFillAfter(true);

        matrixValue = new ColorMatrix();
        matrixSat = new ColorMatrix();

        hsv = intToHSV(myTheme);
        hsvsat[0] = hsv[0];
        hsvsat[1] = 0.8f;
        hsvsat[2] = hsv[2];

        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvvalue[2] = 0.8f;

        hueSeekBar = (org.adw.library.widgets.discreteseekbar.DiscreteSeekBar) rootView.findViewById(R.id.hueSeekBar);
        satSeekBar = (org.adw.library.widgets.discreteseekbar.DiscreteSeekBar) rootView.findViewById(R.id.satSeekBar);
        valueSeekBar = (org.adw.library.widgets.discreteseekbar.DiscreteSeekBar) rootView.findViewById(R.id.valueSeekBar);

        setSeekbarPositions(hsv);

        hueSeekBar.setOnSeekBarChangeListener(this);
        satSeekBar.setOnSeekBarChangeListener(this);
        valueSeekBar.setOnSeekBarChangeListener(this);

        pickerButton.setOnClickListener(this);
        container.setOnClickListener(this);


        setMyColor(myTheme);
        original = myTheme;

        setMyName(colorname);

    }

    public void updateSliders() {

        //recalculate individual values
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);

        //set up arrays for coloring hue and sat bars
        hsvvalue[0] = hsv[0];
        hsvvalue[1] = hsv[1];
        hsvsat[0] = hsv[0];
        hsvsat[2] = hsv[2];

        //convert values into filters
        matrixSat = new ColorMatrix();
        matrixSat.setSaturation(hsv[1]);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixSat);

        //set filters on each seek bar
        hueSeekBar.getTrackDrawable().setColorFilter(filter);
        satSeekBar.getTrackDrawable().setColorFilter(Color.HSVToColor(hsvsat), PorterDuff.Mode.MULTIPLY);
        valueSeekBar.getTrackDrawable().setColorFilter(Color.HSVToColor(hsvvalue), PorterDuff.Mode.MULTIPLY);
        hueSeekBar.setThumbColor(Color.HSVToColor(hsv));
        hueSeekBar.setRippleColor(Color.HSVToColor(hsv));
        satSeekBar.setThumbColor(Color.HSVToColor(hsv));
        satSeekBar.setRippleColor(Color.HSVToColor(hsv));
        valueSeekBar.setThumbColor(Color.HSVToColor(hsv));
        valueSeekBar.setRippleColor(Color.HSVToColor(hsv));
    }

    public float[] intToHSV(int inColor) {
        red = Color.red(myTheme);
        green = Color.green(myTheme);
        blue = Color.blue(myTheme);
        float hsv[] = new float[3];

        Color.RGBToHSV(red, green, blue, hsv);
        return hsv;
    }

    public void toggle_contents() {

        if (isPickerFrameShowing & !isAnimating) {

            mButtonHideSet.start();
            animateButtonColor(original);
            hsv = intToHSV(original);
            setSeekbarPositions(hsv);
            hidePickerFrame();

        } else if (!isPickerFrameShowing & !isAnimating) {

            showPickerFrame();
            int mViewHeight = pickerFrame.getHeight();
            int mValueSliderBottom = valueSeekBar.getBottom();
            down = ObjectAnimator.ofFloat(pickerButton, "translationY", (pickerFrame.getMeasuredHeight() - valueSeekBar.getBottom()) + (pickerButton.getHeight() * .8f));
            mButtonShowSet.play(down).with(left).with(alphaIn);
            setSeekbarPositions(intToHSV(original));
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

    public void animateButtonColor(int themecolor) {
        myTheme = themecolor;
        int colorFrom = pickerButton.getSolidColor();
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, themecolor);
        colorAnimation.setDuration(SPEED_ANIMATION_TRANSITION);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                ShapeDrawable sd = new ShapeDrawable(new OvalShape());
                sd.setIntrinsicHeight(10);
                sd.setIntrinsicWidth(10);
                sd.getPaint().setColor((Integer) animator.getAnimatedValue());
                pickerButton.setBackground(sd);

            }

        });
        colorAnimation.start();
    }

    public void setMyName(String name) {
        colorname = name;
    }

    public void setSeekbarPositions(float hsv[]) {
        hue = Math.round(hsv[0]);
        sat = Math.round(hsv[1] * 100);
        value = Math.round(hsv[2] * 100);
        ObjectAnimator animationHue = ObjectAnimator.ofInt(hueSeekBar, "progress", hue);
        ObjectAnimator animationSat = ObjectAnimator.ofInt(satSeekBar, "progress", sat);
        ObjectAnimator animationValue = ObjectAnimator.ofInt(valueSeekBar, "progress", value);
        animationHue.setDuration(SPEED_ANIMATION_TRANSITION + 100);
        animationSat.setDuration(SPEED_ANIMATION_TRANSITION + 100);
        animationValue.setDuration(SPEED_ANIMATION_TRANSITION + 100);
        animationHue.setInterpolator(new DecelerateInterpolator());
        animationValue.setInterpolator(new DecelerateInterpolator());
        animationSat.setInterpolator(new DecelerateInterpolator());
        animationHue.start();
        animationSat.start();
        animationValue.start();
        hueSeekBar.setProgress(hue);
        satSeekBar.setProgress(sat);
        valueSeekBar.setProgress(value);
    }

    public void showPickerFrame() {

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pickerFrame.getLayoutParams();
        ValueAnimator showAnimator = ValueAnimator.ofInt(params.bottomMargin, 0);
        showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                pickerFrame.requestLayout();
            }

        });
        showAnimator.setDuration(SPEED_ANIMATION_TRANSITION);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isPickerFrameShowing = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showAnimator.start();


    }

    public void hidePickerFrame() {

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pickerFrame.getLayoutParams();
        ValueAnimator hideAnimator = ValueAnimator.ofInt(params.bottomMargin, -1000);
        hideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                pickerFrame.requestLayout();
            }
        });
        hideAnimator.setDuration(SPEED_ANIMATION_TRANSITION);
        hideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isPickerFrameShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        hideAnimator.start();
        isPickerFrameShowing = false;
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

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {


        if (seekBar.getId() == R.id.hueSeekBar) {
            hue = progress;
            hsv[0] = (float) hue;
            hueSeekBar.setIndicatorColor(Color.HSVToColor(hsv));

        } else if (seekBar.getId() == R.id.satSeekBar) {

            sat = progress;
            hsv[1] = (float) sat / 100;
            satSeekBar.setIndicatorColor(Color.HSVToColor(hsv));

        } else if (seekBar.getId() == R.id.valueSeekBar) {

            value = progress;
            hsv[2] = (float) value / 100;
            valueSeekBar.setIndicatorColor(Color.HSVToColor(hsv));

        }

        setMyColor(Color.HSVToColor(hsv));
        updateSliders();

    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

//    @Override
//    public void onViewAttachedToWindow(View v) {
//        Logger.d("XCPP: view attached - " + v);
//        pickerFrame.requestLayout();
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(View v) {
//        Logger.d("XCPP: view detached - " + v);
//        pickerFrame.invalidate();
//    }
}
