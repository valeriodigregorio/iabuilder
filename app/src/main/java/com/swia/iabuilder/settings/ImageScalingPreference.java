package com.swia.iabuilder.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.preference.Preference;

import com.swia.iabuilder.R;

public class ImageScalingPreference extends IntegerPreference {
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 3;

    public ImageScalingPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ImageScalingPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageScalingPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageScalingPreference(Context context) {
        super(context);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int value = Integer.parseInt((String) newValue);
        if (value < MIN_VALUE || value > MAX_VALUE) {
            Toast.makeText(getContext(), getContext().getString(R.string.error_invalid_value, MIN_VALUE, MAX_VALUE), Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onPreferenceChange(preference, newValue);
    }
}
