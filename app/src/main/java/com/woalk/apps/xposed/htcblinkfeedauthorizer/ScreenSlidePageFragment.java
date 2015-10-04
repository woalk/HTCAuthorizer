package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {

    public ScreenSlidePageFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.welcome, container, false);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.titleWelcome);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.summaryWelcome);
        ImageView iconImageView = (ImageView) rootView.findViewById(R.id.iconWelcome);
        Bundle bundle = getArguments();
        titleTextView.setText(bundle.getString("title"));
        summaryTextView.setText(bundle.getString("summary"));
        iconImageView.setBackground(getResources().getDrawable(bundle.getInt("icon"), null));


        return rootView;
    }


}