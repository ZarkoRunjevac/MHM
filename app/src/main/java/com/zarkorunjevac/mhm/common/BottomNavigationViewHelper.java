package com.zarkorunjevac.mhm.common;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by zarko.runjevac on 11/18/2016.
 */

public class BottomNavigationViewHelper {

    public static void disableShiftMode(BottomNavigationView view, int checked) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                if(i==checked){
                    item.setChecked(true);
                }else{
                    item.setChecked(false);
                }

                //Log.d("BottomNavigationViewH", "disableShiftMode:title="+item.getItemData().getTitle()+" item.getItemData().isChecked()="+item.getItemData().isChecked());

            }
        } catch (NoSuchFieldException e) {
           // Log.e(TAG, "disableShiftMode: ", );
            //Log.e( "Unable to get shift mode field",e);
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
    public static void resetActiveButton(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field activeButton = menuView.getClass().getDeclaredField("mActiveButton");
            activeButton.setAccessible(true);
            activeButton.setInt(menuView,0);

        } catch (NoSuchFieldException e) {
            // Log.e(TAG, "disableShiftMode: ", );
            //Log.e( "Unable to get shift mode field",e);
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
        Log.d("BottomNavigationViewH", "------------------------");
    }
    public static void setActiveButton(BottomNavigationView view,int active) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field activeButton = menuView.getClass().getDeclaredField("mActiveButton");
            activeButton.setAccessible(true);
            activeButton.setInt(menuView,active);

        } catch (NoSuchFieldException e) {
            // Log.e(TAG, "disableShiftMode: ", );
            //Log.e( "Unable to get shift mode field",e);
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
}