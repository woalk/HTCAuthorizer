package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ThemeUpdateReceiver extends BroadcastReceiver {
    public ThemeUpdateReceiver() {

    }






    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when this BroadcastReceiver receives an Intent broadcast.
        Bundle extras = intent.getExtras();
        if (intent.getAction().equals("com.woalk.HTCAuthorizer.UPDATE_XML")) {
            if (extras != null) {
                SharedPreferences sharedPref = context.getSharedPreferences("com.woalk.apps.xposed.htcblinkfeedauthorizer_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (extras.containsKey("systemui_color1")) {
                    editor.putInt("systemui_color1", extras.getInt("systemui_color1"));
                }
                if (extras.containsKey("systemui_color2")) {
                    editor.putInt("systemui_color2", extras.getInt("systemui_color2"));
                }
                if (extras.containsKey("systemui_color3")) {
                    editor.putInt("systemui_color3", extras.getInt("systemui_color3"));
                }
                if (extras.containsKey("systemui_color4")) {
                    editor.putInt("systemui_color4", extras.getInt("systemui_color4"));
                }
                editor.apply();
                Logger.d("ThemeFragment: Intent received");
            }
        }
    }
}