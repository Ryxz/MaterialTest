package com.ryx.materialtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ryx on 2019/1/5.
 */
public class MyApplicationContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        if (context == null) {
            context = new MyApplicationContext();
        }
        return context;
    }
}
