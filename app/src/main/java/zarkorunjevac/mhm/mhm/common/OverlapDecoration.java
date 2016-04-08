package zarkorunjevac.mhm.mhm.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by zarko.runjevac on 4/7/2016.
 */
public class OverlapDecoration extends RecyclerView.ItemDecoration {

    private final static int vertOverlap = -200;
    protected final String TAG =
            getClass().getSimpleName();
    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        final int itemPosition = parent.getChildAdapterPosition(view);
        Log.d(TAG, "getItemOffsets: itemPosition="+itemPosition);
        if (itemPosition == 0) {
            Log.d(TAG, "getItemOffsets: ");

            outRect.set(0, 0, 0, 0);;
        }else{
            outRect.set(0, vertOverlap, 0, 0);
        }


    }
}