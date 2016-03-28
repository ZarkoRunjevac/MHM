package zarkorunjevac.mhm.mhm.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import zarkorunjevac.mhm.mhm.AsyncTask.DownloadLatestAsyncTask;
import zarkorunjevac.mhm.mhm.AsyncTask.DownloadLatestOps;
import zarkorunjevac.mhm.mhm.AsyncTask.DownloadPopularAsyncTask;
import zarkorunjevac.mhm.mhm.AsyncTask.DownloadPopularOps;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.GenericPresenter;
import zarkorunjevac.mhm.mhm.model.Music;
import zarkorunjevac.mhm.mhm.model.TrackDownloadModel;
import zarkorunjevac.mhm.mhm.model.Track;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class MusicPresenter extends GenericPresenter<MVP.RequiredPresenterOps,
        MVP.ProvidedMusicModelOps,
        TrackDownloadModel>
        implements MVP.ProvidedMusicPresenterOps,
        MVP.RequiredPresenterOps {

    public WeakReference<MVP.RequiredViewOps> mView;

    private int mNumListToHandle;

    private int mNumListHandled;

    private Music mMusic;

    private  ConcurrentHashMap<String,List<Track>> mDownloadedTracks;


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
    public void startProcessing() {
        //no-op

    }

    @Override
    public void startProcessing(List<String> latest, List<String> popular) {
        mView.get().displayProgressBar();

        mNumListToHandle=latest.size()+popular.size();

        mDownloadedTracks=new ConcurrentHashMap<String, List<Track>>();
        final CountDownLatch exitBarrier=new CountDownLatch(mNumListToHandle);

        ThreadPoolExecutor downloadExecutor=new ThreadPoolExecutor(mNumListToHandle,mNumListToHandle,
                                                0L, TimeUnit.MILLISECONDS,
                                                new LinkedBlockingQueue<Runnable>());

        DownloadLatestAsyncTask downloadLatestAsyncTask;
        DownloadLatestOps downloadLatestOps;

        DownloadPopularAsyncTask downloadPopularAsyncTask;
        DownloadPopularOps downloadPopularOps;

        for(String tracklist : latest){
            downloadLatestOps=new DownloadLatestOps(this,getActivityContext(),exitBarrier,mDownloadedTracks,1,20);
            downloadLatestAsyncTask=new DownloadLatestAsyncTask(downloadLatestOps);
            downloadLatestAsyncTask.executeOnExecutor(downloadExecutor,tracklist);
        }

        for(String trackList : popular){
            downloadPopularOps=new DownloadPopularOps(this,getActivityContext(),exitBarrier,mDownloadedTracks,1,20);
            downloadPopularAsyncTask=new DownloadPopularAsyncTask(downloadPopularOps);
            downloadPopularAsyncTask.executeOnExecutor(downloadExecutor,trackList);


        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    exitBarrier.await();

                }catch (Exception e){
                    Log.d(TAG, "run: ");
                }finally {
                    onProcessingComplete();
                }


            }
        }).start();

    }



    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        resetFields();
        super.onCreate(TrackDownloadModel.class,
                this);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {

        getModel().onDestroy(isChangingConfigurations);

    }

    @Override
    public void onProcessingComplete(Uri url, Uri pathToImageFile) {

    }

    @Override
    public void onProcessingComplete() {
        String s="ss";
        Log.d(TAG, "onProcessingComplete: ");
    }

    public MVP.ProvidedMusicModelOps getModel (){return (MVP.ProvidedMusicModelOps) mOpsInstance;}

    private void resetFields(){

        mNumListToHandle=0;

        mNumListHandled=0;

    }
}
