package com.clearfaun.spencerdepas.walkwayz.Activity;

import android.app.Application;
import android.content.Context;

import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessManager;

/**
 * Created by SpencerDepas on 5/9/17.
 */

public class WalkWayzApplication extends Application {

    private static Context context;
    BackendlessManager backendlessManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        WalkWayzApplication.context = getApplicationContext();

        backendlessManager = new BackendlessManager();
        backendlessManager.init(getAppContext());
    }

    public static Context getAppContext() {
        return WalkWayzApplication.context;
    }


}
