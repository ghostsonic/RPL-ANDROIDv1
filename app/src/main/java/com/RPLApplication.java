package com;

import android.app.Application;

/**
 * Created by Juan on 18/01/2017.
 */
public class RPLApplication extends Application{

    private static RPLApplication singleton  = null;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
    public static RPLApplication getInstance(){
        return singleton;
    }
}
