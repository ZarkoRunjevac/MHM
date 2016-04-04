package zarkorunjevac.mhm.mhm.presenter;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import zarkorunjevac.mhm.mhm.asynctask.DownloadLinkFromPageAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLinkFromPageOps;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.GenericPresenter;
import zarkorunjevac.mhm.mhm.model.TrackDownloadModel;

/**
 * Created by zarkorunjevac on 02/04/16.
 */
public class TrackPresenter extends GenericPresenter<MVP.RequiredTrackPresenterOps,
        MVP.ProvidedTrackDownloadModelOps,
        TrackDownloadModel>
        implements MVP.ProvidedTrackPresenterOps,
        MVP.RequiredTrackPresenterOps{

    public WeakReference<MVP.RequiredViewOps> mView;

    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);

        super.onCreate(TrackDownloadModel.class,this);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {

    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        getModel().onDestroy(isChangingConfigurations);
    }

    @Override
    public void startProcessing(String url) {
        DownloadLinkFromPageOps downloadLinkFromPageOps=new DownloadLinkFromPageOps(this);
        DownloadLinkFromPageAsyncTask downloadLinkFromPageAsyncTask=new DownloadLinkFromPageAsyncTask(downloadLinkFromPageOps);

        downloadLinkFromPageAsyncTask.execute(url);


    }

    @Override
    public void onProcessingComplete(List<String> urls) {

    }

    @Override
    public Context getActivityContext() {
        return mView.get().getActivityContext();
    }

    @Override
    public Context getApplicationContext() {
        return mView.get().getApplicationContext();
    }



    public MVP.ProvidedTrackDownloadModelOps getModel(){
        return (MVP.ProvidedTrackDownloadModelOps) mOpsInstance;
    }
}
