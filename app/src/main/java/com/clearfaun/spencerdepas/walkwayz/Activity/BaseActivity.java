package com.clearfaun.spencerdepas.walkwayz.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.clearfaun.spencerdepas.walkwayz.R;

/**
 * Created by SpencerDepas on 5/6/17.
 */

public class BaseActivity extends AppCompatActivity {


    protected void addFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
