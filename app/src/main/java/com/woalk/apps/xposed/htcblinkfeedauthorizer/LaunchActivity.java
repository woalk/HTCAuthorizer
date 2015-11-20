package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class LaunchActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private SharedPreferences sharedPreferences;
    private Drawable mDot1, mDot2, mDot3;
    private Integer[] colors;
    private ArgbEvaluator argbEvaluator;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Common.fixPermissions(getBaseContext());
        if (!(sharedPreferences.getBoolean("SkipWelcome", false))) {
            setContentView(R.layout.activity_launch);
            mPager = (ViewPager) findViewById(R.id.viewPager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setOnPageChangeListener(this);
            findViewById(R.id.skip_welcome).setOnClickListener(this);
            ImageView mViewDot1 = (ImageView) findViewById(R.id.pager1);
            ImageView mViewDot2 = (ImageView) findViewById(R.id.pager2);
            ImageView mViewDot3 = (ImageView) findViewById(R.id.pager3);
            mDot1 = mViewDot1.getDrawable();
            mDot2 = mViewDot2.getDrawable();
            mDot3 = mViewDot3.getDrawable();
            mDot1.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            mDot2.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            mDot3.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            int color1 = Color.rgb(245, 245, 245);
            int color2 = Color.rgb(255, 200, 160);
            int color3 = Color.rgb(204, 218, 251);
            colors = new Integer[3];
            colors[0] = color1;
            colors[1] = color2;
            colors[2] = color3;
            argbEvaluator = new ArgbEvaluator();
        } else {
            setContentView(R.layout.welcome);
            ImageView icon = (ImageView) findViewById(R.id.iconWelcome);
            TextView title = (TextView) findViewById(R.id.titleWelcome);
            TextView summary = (TextView) findViewById(R.id.summaryWelcome);
            title.setText(getResources().getString(R.string.welcome1_title));
            summary.setText(getResources().getString(R.string.welcome1_summary));
            icon.setBackground(getDrawable(R.drawable.ic_warm_welcome_0));
            final Handler handler = new Handler();

            final Runnable r = new Runnable() {
                public void run() {
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            handler.postDelayed(r, 300);


        }


    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SkipWelcome", true);
        editor.apply();
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position < (mPagerAdapter.getCount() - 1) && position < (colors.length - 1)) {

            getWindow().getDecorView().setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

        } else {

            getWindow().getDecorView().setBackgroundColor(colors[colors.length - 1]);

        }

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mDot1.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            mDot2.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mDot3.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        }
        if (position == 1) {
            mDot1.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mDot2.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            mDot3.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        }
        if (position == 2) {
            mDot1.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mDot2.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mDot3.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        int mNumItems = 3;

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }


        // Returns total number of pages
        @Override
        public int getCount() {
            return mNumItems;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                Fragment slidePageFragment = new ScreenSlidePageFragment();
                Bundle argsBundle = new Bundle();
                argsBundle.putString("title", getResources().getString(R.string.welcome1_title));
                argsBundle.putString("summary", getResources().getString(R.string.welcome1_summary));
                argsBundle.putInt("icon", R.drawable.ic_warm_welcome_0);
                slidePageFragment.setArguments(argsBundle);
                return slidePageFragment;
            } else if (position == 1) {
                Fragment slidePageFragment = new ScreenSlidePageFragment();
                Bundle argsBundle = new Bundle();
                argsBundle.putString("title", getResources().getString(R.string.welcome2_title));
                argsBundle.putString("summary", getResources().getString(R.string.welcome2_summary));
                argsBundle.putInt("icon", R.drawable.ic_warm_welcome_2);
                slidePageFragment.setArguments(argsBundle);
                return slidePageFragment;
            } else if (position == 2) {
                Fragment slidePageFragment = new ScreenSlidePageFragment();
                Bundle argsBundle = new Bundle();
                argsBundle.putString("title", getResources().getString(R.string.welcome3_title));
                argsBundle.putString("summary", getResources().getString(R.string.welcome3_summary));
                argsBundle.putInt("icon", R.drawable.ic_warm_welcome_3);
                slidePageFragment.setArguments(argsBundle);
                return slidePageFragment;
            } else {
                return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}

