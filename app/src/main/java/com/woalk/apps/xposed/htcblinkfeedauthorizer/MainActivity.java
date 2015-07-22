package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replaceable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(android.R.id.title);
        setActionBar(toolbar);
        title.setText(toolbar.getTitle());
        //noinspection ConstantConditions
        getActionBar().setTitle(null);

        Fragment f = new MainPreferenceFragment();

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(MainPreferenceFragment.EXTRA_SUBSCREEN_ID)) {
            Bundle args = new Bundle();
            args.putInt(MainPreferenceFragment.EXTRA_SUBSCREEN_ID,
                    getIntent().getExtras().getInt(MainPreferenceFragment.EXTRA_SUBSCREEN_ID));
            f.setArguments(args);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            title.setText(title.getText() + " - " +
                    getString(R.string.pref_general_always_active_window));
        }

        getFragmentManager().beginTransaction().replace(android.R.id.widget_frame, f).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
