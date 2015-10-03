package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ThemeUpdateReceiver extends BroadcastReceiver {
    private int color1, color2, color3, color4, mixcolor;

    public ThemeUpdateReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // This method is called when this BroadcastReceiver receives an Intent broadcast.
        Bundle extras = intent.getExtras();
        if (intent.getAction().equals("com.woalk.HTCAuthorizer.UPDATE_XML")) {

            if (extras != null) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (extras.containsKey("full_Array")) {

                    int[] arrayOfInt = extras.getIntArray("full_Array");
                    editor.putInt("theme_PrimaryColor", arrayOfInt[2]);
                    editor.putInt("theme_AccentColor", arrayOfInt[1]);
                    editor.putInt("theme_PrimaryDarkColor", Common.lightenColor(arrayOfInt[2], -.25f));
                    editor.putInt("theme_Comms_Primary", arrayOfInt[3]);
                    editor.putInt("theme_Comms_Light", arrayOfInt[4]);
                    editor.putInt("theme_Comms_Dark", arrayOfInt[5]);
                    editor.putInt("theme_Info_Primary", arrayOfInt[6]);
                    editor.putInt("theme_Info_Light", arrayOfInt[7]);
                    editor.putInt("theme_Info_Dark", arrayOfInt[8]);
                    editor.putInt("theme_Entertainment_Primary", arrayOfInt[9]);
                    editor.putInt("theme_Entertainment_Light", arrayOfInt[10]);
                    editor.putInt("theme_Entertainment_Dark", arrayOfInt[11]);
                    editor.apply();
                    intent = new Intent(context, MainActivity.class);
                    intent.setComponent(new ComponentName("com.woalk.apps.xposed.htcblinkfeedauthorizer", "com.woalk.apps.xposed.htcblinkfeedauthorizer.MainActivity"));
                    intent.putExtra("toOpen", "themeFragment");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        }
    }
}

