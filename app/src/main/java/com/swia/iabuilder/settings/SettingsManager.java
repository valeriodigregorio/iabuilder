package com.swia.iabuilder.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.swia.iabuilder.R;

public class SettingsManager {
    private static final String TAG = SettingsManager.class.getName();

    private static SharedPreferences settings = null;

    public static void initialize(Context context) {
        if (settings == null) {
            settings = PreferenceManager.getDefaultSharedPreferences(context);
            initializeIntSetting(context.getString(R.string.setting_deck_columns_portrait), 4);
            initializeIntSetting(context.getString(R.string.setting_deck_columns_landscape), 4);
            initializeIntSetting(context.getString(R.string.setting_deck_image_scaling), 2);
            initializeIntSetting(context.getString(R.string.setting_card_browser_columns_portrait), 4);
            initializeIntSetting(context.getString(R.string.setting_card_browser_columns_landscape), 4);
            initializeIntSetting(context.getString(R.string.setting_card_browser_image_scaling), 2);
        }
    }

    private static void initializeIntSetting(String key, int defValue) {
        String setting = settings.getString(key, null);
        if (setting == null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, Integer.toString(defValue));
            editor.apply();
            Log.i(TAG, key + " initialized to " + defValue);
        }
    }

    public static int getIntSetting(String key) {
        String setting = settings.getString(key, null);
        Log.i(TAG, key + " is " + setting);
        return Integer.parseInt(setting);
    }

    public static void setIntSetting(String key, int value) {
        SharedPreferences.Editor editor = settings.edit();
        Log.i(TAG, key + " set to " + value);
        editor.putString(key, Integer.toString(value));
        editor.apply();
    }
}