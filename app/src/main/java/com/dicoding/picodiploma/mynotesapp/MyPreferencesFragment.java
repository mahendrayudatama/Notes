package com.dicoding.picodiploma.mynotesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class MyPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String LANGUAGE;
    private String NOTIFICATION;
    private AlarmReceiver alarmReceiver;
    private Preference languagePreference;
    private SwitchPreferenceCompat notificationPreference;
    String time = "20:45";

    private void init(){
        alarmReceiver = new AlarmReceiver();
        LANGUAGE = getResources().getString(R.string.change_language_settings);
        NOTIFICATION = "Notification";
        languagePreference = findPreference(LANGUAGE);
        notificationPreference = findPreference(NOTIFICATION);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
        onClickPreferences();
    }

    private void onClickPreferences(){
        languagePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
            }
        });
    }

    private void setSummaries(){
        SharedPreferences sum = getPreferenceManager().getSharedPreferences();
        languagePreference.setSummary(sum.getString(LANGUAGE, Locale.getDefault().getDisplayLanguage()));
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        if(key.equals(LANGUAGE)) {
            String DEFAULT_VALUE = "Tidak Ada";
            languagePreference.setSummary(sharedPreferences.getString(LANGUAGE, DEFAULT_VALUE));
        }
        if (key.equals(NOTIFICATION)) {
            if(notificationPreference.isChecked()){
                alarmReceiver.setRepeatingAlarm(
                        requireContext(), time
                );
                Toast.makeText(getContext(), getResources().getString(R.string.toast_success), Toast.LENGTH_SHORT).show();
            }
            else {
                alarmReceiver.cancelAlarm(requireContext());
            }
        }
    }
}