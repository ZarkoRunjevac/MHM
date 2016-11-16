package com.zarkorunjevac.mhm.common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by com.zarkorunjevac.mhm on 18/04/16.
 */
public class TypefaceUtils {

    public static Typeface getTypeFaceHelevticaNeueProMedium(Context context){
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaNeueLTPro-Md.otf");

    }
}
