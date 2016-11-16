package com.zarkorunjevac.mhm.common;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * Defines methods for obtaining Contexts used by all views in the
 * "View" layer.
 */
public interface ContextView {
    /**
     * Get the Activity Context.
     */
    Context getActivityContext();

    /**
     * Get the Application Context.
     */
    Context getApplicationContext();

    FragmentManager getSupportFragmentManager();

}
