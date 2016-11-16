package com.zarkorunjevac.mhm.presenter;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.zarkorunjevac.mhm.MVP;
import com.zarkorunjevac.mhm.R;
import com.zarkorunjevac.mhm.asynctask.DownloadLatestAsyncTask;
import com.zarkorunjevac.mhm.asynctask.DownloadLatestOps;
import com.zarkorunjevac.mhm.asynctask.DownloadLinkFromPageAsyncTask;
import com.zarkorunjevac.mhm.asynctask.DownloadLinkFromPageOps;
import com.zarkorunjevac.mhm.asynctask.DownloadPopularAsyncTask;
import com.zarkorunjevac.mhm.asynctask.DownloadPopularOps;
import com.zarkorunjevac.mhm.common.Config;
import com.zarkorunjevac.mhm.common.GenericPresenter;
import com.zarkorunjevac.mhm.common.MusicPlayerServiceStatus;
import com.zarkorunjevac.mhm.common.TrackListType;
import com.zarkorunjevac.mhm.common.Utils;
import com.zarkorunjevac.mhm.model.TrackModel;
import com.zarkorunjevac.mhm.model.pojo.Track;
import com.zarkorunjevac.mhm.notification.TrackNotificationManager;
import com.zarkorunjevac.mhm.service.MusicTrackResults;
import com.zarkorunjevac.mhm.ui.fragment.PlaybackControlsFragment;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class TrackPresenter
        extends GenericPresenter<MVP.RequiredTrackListPresenterOps,
        MVP.ProvidedTrackListDownloadModelOps,
        TrackModel>
        implements
        MVP.ProvidedTrackListPresenterOps,
        MVP.RequiredTrackListPresenterOps,
        MusicTrackResults {

    private static final int TRACK_LIST_PAGE = 1;
    private static final int TRACK_LIST_COUNT = 12;

    public WeakReference<MVP.RequiredViewOps> mView;

    //private boolean mCallInProgress;

    private int mNumListToHandle;

    private int mNumListHandled;

    MusicPlayerServiceStatus mMusicPlayerServiceStatus;

    private Track mSelectedTrack;

    private PlaybackControlsFragment mControlsFragment;


    private ConcurrentHashMap<String, List<Track>> mDownloadedTracks;

    private String mTrackListName;

    private TrackListType mTrackListType;

    private TrackNotificationManager mTrackNotificationManager;


    public TrackPresenter() {
    }

    @Override
    public void onCreate(MVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        resetFields();

        //initializeMediaPlayer();
        initializePlaybackControlsFragment();
        super.onCreate(TrackModel.class,
                this);

        hidePlaybackControls();
        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.STOPPED;

        mTrackNotificationManager=new TrackNotificationManager(this);
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
        Log.d(TAG, "onConfigurationChange: mDownloadedTracks.size()=" + mDownloadedTracks.size());

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
    public FragmentManager getSupportFragmentManager() {
        return mView.get().getSupportFragmentManager();
    }


    @Override
    public void startTrackListDownload(List<String> latest, List<String> popular) {
        mView.get().displayProgressBar();

        int latestNum, popularNum;
        //(mBlogs == null) ? 0 : mBlogs.size();
        latestNum = (latest == null) ? 0 : latest.size();
        popularNum = (popular == null) ? 0 : popular.size();
        mNumListToHandle = latestNum + popularNum;

        mDownloadedTracks = new ConcurrentHashMap<String, List<Track>>();
        //final CountDownLatch exitBarrier=new CountDownLatch(mNumListToHandle);

        ThreadPoolExecutor downloadExecutor = new ThreadPoolExecutor(mNumListToHandle, mNumListToHandle,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        DownloadLatestAsyncTask downloadLatestAsyncTask;
        DownloadLatestOps downloadLatestOps;

        DownloadPopularAsyncTask downloadPopularAsyncTask;
        DownloadPopularOps downloadPopularOps;

        for (String tracklist : latest) {
            downloadLatestOps = new DownloadLatestOps(this, getActivityContext(), mDownloadedTracks, TRACK_LIST_PAGE, TRACK_LIST_COUNT);
            downloadLatestAsyncTask = new DownloadLatestAsyncTask(downloadLatestOps);
            downloadLatestAsyncTask.executeOnExecutor(downloadExecutor, tracklist);
        }

        for (String trackList : popular) {
            downloadPopularOps = new DownloadPopularOps(this, getActivityContext(), mDownloadedTracks, TRACK_LIST_PAGE, TRACK_LIST_COUNT);
            downloadPopularAsyncTask = new DownloadPopularAsyncTask(downloadPopularOps);
            downloadPopularAsyncTask.executeOnExecutor(downloadExecutor, trackList);


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
//       Log.d(TAG, "onTrackListDownloadComplete: mNumListToHandle=" + mNumListToHandle);
//        Log.d(TAG, "onTrackListDownloadComplete: mNumListHandled=" + mNumListHandled);
        if (mDownloadedTracks.get(listName) == null) {
            // TODO add this string to string.xml
//            Log.d(TAG, "onTrackListDownloadComplete: listname=" + listName);
            mView.get().reportDownloadFailure(listName);

        }

        tryToDisplayLists();
    }

    private void tryToDisplayLists() {
        if (allDownloadsComplete()) {
            // Dismiss the progress bar.
            mView.get().dismissProgressBar();

            // Initialize state for the next run.
            resetFields();
//            Log.d(TAG, "tryToDisplayLists: mDownloadedTracks.size()=" + mDownloadedTracks.size());
            mView.get().dispayResults(mDownloadedTracks);
        }
    }


    @Override
    public void onDestroy(boolean isChangingConfigurations) {

        getModel().onDestroy(isChangingConfigurations);
    }

    @Override
    public void setTrackListParams(String trackListName, TrackListType trackListType) {
        mTrackListName = trackListName;
        mTrackListType = trackListType;
    }

    @Override
    public String getTrackListName() {
        return mTrackListName;
    }

    @Override
    public TrackListType getTrackListType() {
        return mTrackListType;
    }

    public MVP.ProvidedTrackListDownloadModelOps getModel() {
        return (MVP.ProvidedTrackListDownloadModelOps) mOpsInstance;
    }

    private void resetFields() {

        mNumListToHandle = 0;

        mNumListHandled = 0;


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
        return mNumListToHandle > 0;
    }

    @Override
    public void startTrackDownload(Track track) {
        mSelectedTrack = track;

        DownloadLinkFromPageOps downloadLinkFromPageOps = new DownloadLinkFromPageOps(this);
        DownloadLinkFromPageAsyncTask downloadLinkFromPageAsyncTask = new DownloadLinkFromPageAsyncTask(downloadLinkFromPageOps);
        mView.get().displayProgressBar();
        downloadLinkFromPageAsyncTask.execute(track);
    }

    @Override
    public void onTrackDownloadComplete(String link) {
        if (link != null) {
            mSelectedTrack.setStreamUrl(link + "?client_id=" + Config.CLIENT_ID);
            //initializeMediaPlayer();
            MVP.ProvidedPlaybackControlsFragmentOps providedPlaybackControlsFragmentOps = (MVP.ProvidedPlaybackControlsFragmentOps) mControlsFragment;
            mControlsFragment.initializeViewFields(mSelectedTrack);
            Utils.showToast(getActivityContext(), mSelectedTrack.getStreamUrl());

            //showPlaybackFragment();
            playMedia(mSelectedTrack);
           //mControlsFragment.displayPlayButton();


        }else {
            mView.get().dismissProgressBar();
        }

    }

    @Override
    public void takePage(int page, TrackListType trackListType, String trackListName) {

        mNumListToHandle = 1;
        mDownloadedTracks = new ConcurrentHashMap<String, List<Track>>();
        if (trackListType == TrackListType.LATEST) {

            DownloadLatestAsyncTask downloadLatestAsyncTask;
            DownloadLatestOps downloadLatestOps;

            downloadLatestOps = new DownloadLatestOps(this, getActivityContext(), mDownloadedTracks, page, 0);
            downloadLatestAsyncTask = new DownloadLatestAsyncTask(downloadLatestOps);
            downloadLatestAsyncTask.execute(trackListName);

        } else {
            DownloadPopularAsyncTask downloadPopularAsyncTask;
            DownloadPopularOps downloadPopularOps;

            downloadPopularOps = new DownloadPopularOps(this, getActivityContext(), mDownloadedTracks, page, 0);
            downloadPopularAsyncTask = new DownloadPopularAsyncTask(downloadPopularOps);

            downloadPopularAsyncTask.execute(trackListName);
        }
    }

    @Override
    public void togglePlayPause() {

        if(mMusicPlayerServiceStatus==MusicPlayerServiceStatus.PAUSED){

            getModel().playMedia(this);
            mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PLAYING;
        }else{
            getModel().pauseMedia(this);
            mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PAUSED;
        }
    }
    @Override
    public void playMedia(){
        getModel().playMedia(this);
        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PLAYING;
    }
    @Override
    public void pauseMedia(){
        getModel().pauseMedia(this);
        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PAUSED;
    }



    @Override
    public void playMedia(Track track) {
        Log.d(TAG, "playMedia: "+track.getTitle());
        if (mMusicPlayerServiceStatus==MusicPlayerServiceStatus.PLAYING){
            getModel().pauseMedia(this);
        }
       // mView.get().dismissProgressBar();
        getModel().startToPlayMedia(track,this);
        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PLAYING;
        mTrackNotificationManager.startNotification();
    }

    @Override
    public Track getSelectedTrack() {
        return mSelectedTrack;
    }


    public MusicPlayerServiceStatus getStatus(){
        return mMusicPlayerServiceStatus;
    }



    private void initializePlaybackControlsFragment() {
        mControlsFragment = (PlaybackControlsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);

        if (mControlsFragment == null) {
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        }


    }


    private void showPlaybackFragment() {
        Log.d(TAG, "showPlaybackFragment: ");
        getSupportFragmentManager().beginTransaction()

//                .setCustomAnimations(R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
//                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                .show(mControlsFragment)
                .commit();
    }

    protected void hidePlaybackControls() {
        Log.d(TAG, "hidePlaybackControls");
        getSupportFragmentManager().beginTransaction()
                .hide(mControlsFragment)
                .commit();
    }

    @Override
    public void playing(String isPlaying) throws RemoteException {
        Log.d(TAG, "playing: ");
        showPlaybackFragment();
        //
        mControlsFragment.displayPauseButton();
        mView.get().dismissProgressBar();
        mTrackNotificationManager.updateNotification();
    }

    @Override
    public void paused(String isPaused) throws RemoteException {
        Log.d(TAG, "paused: ");

        mControlsFragment.displayPlayButton();
        mTrackNotificationManager.updateNotification();
    }

    @Override
    public void stopped(String isStopped) throws RemoteException {

    }



    @Override
    public IBinder asBinder() {
        return null;
    }
}
