package com.swia.iabuilder.settings;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

public class IntegerPreference extends EditTextPreference implements EditTextPreference.OnBindEditTextListener, Preference.OnPreferenceChangeListener {

    private String summary = null;

    private void initialize() {
        summary = getSummary().toString();
        setIconSpaceReserved(false);
        setOnBindEditTextListener(this);
        setOnPreferenceChangeListener(this);
        setSummary(String.format(summary, SettingsManager.getIntSetting(getKey())));
    }

    public IntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public IntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public IntegerPreference(Context context) {
        super(context);
        initialize();
    }

    @Override
    public void onBindEditText(@NonNull EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(String.format(summary, Integer.parseInt((String) newValue)));
        return true;
    }
}
