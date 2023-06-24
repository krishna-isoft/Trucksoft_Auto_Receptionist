package com.isoft.trucksoft_autoreceptionist;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


/**
 * Created by isoft on 26/10/17.
 */

public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
