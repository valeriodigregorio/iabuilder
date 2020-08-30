package com.swia.iabuilder.datastores;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigStore {

    private static final String CONFIG_STORE = "config_store";

    private static final String CONFIG_SHOW_DISABLED = "config_show_disabled";
    private static final String CONFIG_CURRENT_RELEASE = "config_current_release";

    private static SharedPreferences preferences = null;

    public static void initialize(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(CONFIG_STORE, 0);
        }
    }

    public static boolean getShowDisabled() {
        return preferences.getBoolean(CONFIG_SHOW_DISABLED, false);
    }

    public static int getCurrentRelease() {
        return preferences.getInt(CONFIG_CURRENT_RELEASE, 0);
    }

    public static void setShowDisabled(boolean showDisabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CONFIG_SHOW_DISABLED, showDisabled);
        editor.apply();
    }

    public static void setCurrentRelease(int code) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CONFIG_CURRENT_RELEASE, code);
        editor.apply();
    }
}