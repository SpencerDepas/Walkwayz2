package com.clearfaun.spencerdepas.walkwayz.Activity;

import android.app.Application;
import android.content.Context;

import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessManager;
import com.clearfaun.spencerdepas.walkwayz.R;
import com.hypertrack.lib.HyperTrack;

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

        initHyperTrack();
    }

    private void initHyperTrack(){
        HyperTrack.initialize(this, context.getString(R.string.hypertrack_publishable_key));
    }

    public static Context getAppContext() {
        return WalkWayzApplication.context;
    }


}
