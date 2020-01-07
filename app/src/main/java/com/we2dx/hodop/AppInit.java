package com.we2dx.hodop;

import android.app.Application;

import com.google.android.libraries.places.api.Places;

import java.util.Locale;

import timber.log.Timber;

public class AppInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.map_api), Locale.US);
        }
    }
}
