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

import zarkorunjevac.mhm.mhm.asynctask.DownloadLatestAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLatestOps;
import zarkorunjevac.mhm.mhm.asynctask.DownloadPopularAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadPopularOps;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.GenericPresenter;
import zarkorunjevac.mhm.mhm.model.pojo.Music;
import zarkorunjevac.mhm.mhm.model.TrackDownloadModel;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

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
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        resetFields();
        super.onCreate(TrackDownloadModel.class,
                this);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);

        if (allDownloadsComplete()) {
            // Hide the progress bar.
            mView.get().dismissProgressBar();
            Log.d(TAG,
                    "All lists have finished downloading");
        } else if (downloadsInProgress()) {
            // Display the progress bar.
            mView.get().displayProgressBar();

            Log.d(TAG,
                    "Not all lists have finished downloading");
        }

        // (Re)display the URLs.
        mView.get().displayUrls();
        mView.get().dispayResults(mDownloadedTracks);
    }

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

        int latestNum,popularNum;
        //(mBlogs == null) ? 0 : mBlogs.size();
        latestNum=(latest==null) ? 0 : latest.size();
        popularNum=(popular==null) ? 0 :popular.size();
        mNumListToHandle=latestNum+popularNum;

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
            downloadLatestOps=new DownloadLatestOps(this,getActivityContext(),mDownloadedTracks,1,6);
            downloadLatestAsyncTask=new DownloadLatestAsyncTask(downloadLatestOps);
            downloadLatestAsyncTask.executeOnExecutor(downloadExecutor,tracklist);
        }

        for(String trackList : popular){
            downloadPopularOps=new DownloadPopularOps(this,getActivityContext(),mDownloadedTracks,1,6);
            downloadPopularAsyncTask=new DownloadPopularAsyncTask(downloadPopularOps);
            downloadPopularAsyncTask.executeOnExecutor(downloadExecutor,trackList);


        }


        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    exitBarrier.await();

                }catch (Exception e){
                    Log.d(TAG, "run: ");
                }finally {
                    //onProcessingComplete();
                }
             }
        }).start();*/

    }

    @Override
    public void onProcessingComplete(String listName) {

        ++mNumListHandled;

        if(mDownloadedTracks.get(listName)==null){
            // TODO add this string to string.xml

            mView.get().reportDownloadFailure(listName);

        }else
        {
            Log.d(TAG, "onProcessingComplete: received list "+listName);
        }

        tryToDisplayLists();
    }

    private void tryToDisplayLists(){
        if (allDownloadsComplete()) {
            // Dismiss the progress bar.
            mView.get().dismissProgressBar();

            // Initialize state for the next run.
            resetFields();

            mView.get().dispayResults(mDownloadedTracks);
        }
    }



    @Override
    public void onDestroy(boolean isChangingConfigurations) {

        getModel().onDestroy(isChangingConfigurations);

    }

    @Override
    public void onProcessingComplete(Uri url, Uri pathToImageFile) {

    }



    public MVP.ProvidedMusicModelOps getModel (){return (MVP.ProvidedMusicModelOps) mOpsInstance;}

    private void resetFields(){

        mNumListToHandle=0;

        mNumListHandled=0;

    }

    /* Returns true if all the downloads have completed, else false.
            */
    private boolean allDownloadsComplete() {
        return mNumListHandled == mNumListToHandle
                && mNumListHandled > 0;
    }

    /**
     * Returns true if there are any downloads in progress, else false.
     */
    private boolean downloadsInProgress() {
        return  mNumListToHandle > 0;
    }



}
