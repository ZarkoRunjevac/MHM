package zarkorunjevac.mhm.mhm.utils.loader;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Container that holds an image view in a weak reference.
 */
public class ImageViewHolder {
    /**
     * WeakReference to an ImageView object to enable garbage
     * collection.
     */
    protected WeakReference<ImageView> mImgView;

    /**
     * Constructor initializes the field.
     */
    public ImageViewHolder(ImageView imgView) {
        mImgView = new WeakReference<ImageView>(imgView);
    }

    /**
     * Getter for the wrapped ImageView. isCollected() should be
     * called before calling this method.
     */
    public ImageView getWrappedImageView() {
        return mImgView.get();
    }

    /**
     * Returns true if the wrapped ImageView has been garbage
     * collected, false otherwise.
     */
    public boolean isCollected() {
        return mImgView.get() == null;
    }
}
