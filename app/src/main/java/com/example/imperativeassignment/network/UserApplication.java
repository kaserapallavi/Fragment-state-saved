package com.example.imperativeassignment.network;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public class UserApplication extends Application {
    public static final String TAG = UserApplication.class.getSimpleName();

    private static UserApplication mInstance;
    private static Context appContext;

    private static ApiService retroApiClient;


    public UserApplication() {
        super();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static UserApplication getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("UserApplication not instantiated.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mInstance = this;
        appContext = this.getApplicationContext();
        retroApiClient = APIClient.getClient().create(ApiService.class);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static ApiService getRetroApiClient() {
        if (retroApiClient == null) {
            retroApiClient = APIClient.getClient().create(ApiService.class);
        }
        return retroApiClient;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        newConfig.fontScale = 1;
        super.onConfigurationChanged(newConfig);
    }

}