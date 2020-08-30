package com.swia.iabuilder.settings;

import android.content.Context;
import android.util.AttributeSet;

public class PreferenceCategory extends androidx.preference.PreferenceCategory {

    private void initialize() {
        setIconSpaceReserved(false);
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PreferenceCategory(Context context) {
        super(context);
        initialize();
    }
}
