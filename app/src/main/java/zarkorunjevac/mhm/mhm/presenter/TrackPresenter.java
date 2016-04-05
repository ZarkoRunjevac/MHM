package zarkorunjevac.mhm.mhm.presenter;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLatestAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLatestOps;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLinkFromPageAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadLinkFromPageOps;
import zarkorunjevac.mhm.mhm.asynctask.DownloadPopularAsyncTask;
import zarkorunjevac.mhm.mhm.asynctask.DownloadPopularOps;
import zarkorunjevac.mhm.mhm.common.GenericPresenter;
import zarkorunjevac.mhm.mhm.model.TrackModel;
import zarkorunjevac.mhm.mhm.model.pojo.Music;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class TrackPresenter extends GenericPresenter<MVP.RequiredTrackListPresenterOps,
        MVP.ProvidedTrackListDownloadModelOps,
        TrackModel>
        implements MVP.ProvidedTrackListPresenterOps,
        MVP.RequiredTrackListPresenterOps {

    private static final int TRACK_LIST_PAGE = 1;
    private static final int TRACK_LIST_COUNT = 100;
    public WeakReference<MVP.RequiredViewOps> mView;

    private int mNumListToHandle;

    private int mNumListHandled;

    private Music mMusic;

    private  ConcurrentHashMap<String,List<Track>> mDownloadedTracks;


    public TrackPresenter(){}

    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        resetFields();
        super.onCreate(TrackModel.class,
                this);
    }

    @Override
    public void onConfigurationChange(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);

        if (allDownloadsComplete()) {
            // Hide the progress bar.
            mView.get().dismissProgressBar();
            Log.d(TAG, "onConfigurationChange: All lists have finished downloading");



        } else if (downloadsInProgress()) {
            // Display the progress bar.
            mView.get().displayProgressBar();

            Log.d(TAG,
                    "Not all lists have finished downloading");
        }
        Log.d(TAG, "onConfigurationChange: mDownloadedTracks.size()="+mDownloadedTracks.size());
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
    public void startTrackListDownload(List<String> latest, List<String> popular) {
        mView.get().displayProgressBar();

        int latestNum,popularNum;
        //(mBlogs == null) ? 0 : mBlogs.size();
        latestNum=(latest==null) ? 0 : latest.size();
        popularNum=(popular==null) ? 0 :popular.size();
        mNumListToHandle=latestNum+popularNum;

        mDownloadedTracks=new ConcurrentHashMap<String, List<Track>>();
        //final CountDownLatch exitBarrier=new CountDownLatch(mNumListToHandle);

        ThreadPoolExecutor downloadExecutor=new ThreadPoolExecutor(mNumListToHandle,mNumListToHandle,
                                                0L, TimeUnit.MILLISECONDS,
                                                new LinkedBlockingQueue<Runnable>());

        DownloadLatestAsyncTask downloadLatestAsyncTask;
        DownloadLatestOps downloadLatestOps;

        DownloadPopularAsyncTask downloadPopularAsyncTask;
        DownloadPopularOps downloadPopularOps;

        for(String tracklist : latest){
            downloadLatestOps=new DownloadLatestOps(this,getActivityContext(),mDownloadedTracks,TRACK_LIST_PAGE,TRACK_LIST_COUNT);
            downloadLatestAsyncTask=new DownloadLatestAsyncTask(downloadLatestOps);
            downloadLatestAsyncTask.executeOnExecutor(downloadExecutor,tracklist);
        }

        for(String trackList : popular){
            downloadPopularOps=new DownloadPopularOps(this,getActivityContext(),mDownloadedTracks,TRACK_LIST_PAGE,TRACK_LIST_COUNT);
            downloadPopularAsyncTask=new DownloadPopularAsyncTask(downloadPopularOps);
            downloadPopularAsyncTask.executeOnExecutor(downloadExecutor,trackList);


        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//
//                    exitBarrier.await();
//
//                }catch (Exception e){
//                    Log.d(TAG, "run: ");
//                }finally {
//                    //onTrackListDownloadComplete();
//                }
//             }
//        }).start();
    }

    @Override
    public void onTrackListDownloadComplete(String listName) {

        ++mNumListHandled;
        Log.d(TAG, "onTrackListDownloadComplete: mNumListToHandle="+mNumListToHandle);
        Log.d(TAG, "onTrackListDownloadComplete: mNumListHandled="+mNumListHandled);
        if(mDownloadedTracks.get(listName)==null){
            // TODO add this string to string.xml
            Log.d(TAG, "onTrackListDownloadComplete: listname="+listName);
            mView.get().reportDownloadFailure(listName);

        }

        tryToDisplayLists();
    }

    private void tryToDisplayLists(){
        if (allDownloadsComplete()) {
            // Dismiss the progress bar.
            mView.get().dismissProgressBar();

            // Initialize state for the next run.
            resetFields();
            Log.d(TAG, "tryToDisplayLists: mDownloadedTracks.size()="+mDownloadedTracks.size());
            mView.get().dispayResults(mDownloadedTracks);
        }
    }



    @Override
    public void onDestroy(boolean isChangingConfigurations) {

        getModel().onDestroy(isChangingConfigurations);

    }





    public MVP.ProvidedTrackListDownloadModelOps getModel (){return (MVP.ProvidedTrackListDownloadModelOps) mOpsInstance;}

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

    @Override
    public void startTrackDownload(Track track) {
        DownloadLinkFromPageOps downloadLinkFromPageOps=new DownloadLinkFromPageOps(this);
        DownloadLinkFromPageAsyncTask downloadLinkFromPageAsyncTask=new DownloadLinkFromPageAsyncTask(downloadLinkFromPageOps);

        downloadLinkFromPageAsyncTask.execute(track);
    }

    @Override
    public void onTrackDownloadComplete(String link) {
            if(link!=null){
                mView.get().returnTrackLink(link);
            }
    }
}
