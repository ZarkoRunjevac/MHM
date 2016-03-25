package zarkorunjevac.mhm.mhm.presenter;

import android.content.Context;
import android.net.Uri;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.GenericPresenter;
import zarkorunjevac.mhm.mhm.model.MusicDownloadModel;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class MusicPresenter extends GenericPresenter<MVP.RequiredPresenterOps,
        MVP.ProvidedMusicModelOps,
        MusicDownloadModel>
        implements MVP.ProvidedPresenterOps,
        MVP.RequiredPresenterOps {

    public WeakReference<MVP.RequiredViewOps> mView;

    public MusicPresenter(){}

    @Override
    public Context getActivityContext() {
        return mView.get().getActivityContext();
    }

    @Override
    public Context getApplicationContext() {
        return mView.get().getApplicationContext();
    }

    @Override
    public ArrayList<Uri> getUrlList() {
        return null;
    }

    @Override
    public void startProcessing() {
        mView.get().displayProgressBar();
        // Iterate over all the URLs, start each download in an
        // AsyncTask, apply a grayscale filter to each image
        // that's downloaded successfully, and finally call
        // onProcessingComplete() when all is done.  Each pair of
        // downloading and filtering should each be performed in
        // two separate AsyncTask instances, which should run
        // concurrently via the AsyncTask.THREAD_POOL_EXECUTOR and
        // executeOnExecutor().

        // TODO -- you fill  in here.

    }

    @Override
    public void deleteDownloadedImages() {

    }

    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {

    }

    @Override
    public void onProcessingComplete(Uri url, Uri pathToImageFile) {

    }

    public MVP.ProvidedMusicModelOps getModel (){return (MVP.ProvidedMusicModelOps) mOpsInstance;}
}
