package zarkorunjevac.mhm.mhm.model;

import android.content.Context;

import java.lang.ref.WeakReference;

import zarkorunjevac.mhm.mhm.MVP;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class MusicDownloadModel
        implements MVP.ProvidedMusicModelOps {

    protected final static String TAG =
            MusicDownloadModel.class.getSimpleName();

    /**
     * A WeakReference used to access methods in the Presenter layer.
     * The WeakReference enables garbage collection.
     */
    private WeakReference<MVP.RequiredPresenterOps> mPresenter;

    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        mPresenter =
                new WeakReference<>(presenter);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {

    }

    @Override
    public Music downloadMusic(Context context) {
        return null;
    }
}
