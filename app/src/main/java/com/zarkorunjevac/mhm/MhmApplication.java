package com.zarkorunjevac.mhm;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by zarko.runjevac on 6/14/2016.
 */
public class MhmApplication extends Application {

    private static MhmApplication instance;
    protected final  String TAG =
            getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Log.i(TAG, "onCreate: Creating Application");
    }

    public static MhmApplication getInstance ()
    {
        return instance;
    }

    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
