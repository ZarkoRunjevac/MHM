package com.zarkorunjevac.mhm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.zarkorunjevac.mhm.R;
import com.zarkorunjevac.mhm.common.MusicPlayerServiceStatus;
import com.zarkorunjevac.mhm.common.Utils;
import com.zarkorunjevac.mhm.model.pojo.Track;
import com.zarkorunjevac.mhm.ui.activity.FullScreenPlayerActivity;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicPlayerService extends Service {

    private ExecutorService mExecutorService;

    private MediaPlayer mMediaPlayer=null;

    private Track mSelectedTrack;

    private NotificationManager mNotificationManager;

    private Notification mNotification = null;

    final CountDownLatch mMediaPlayerBarrier=new CountDownLatch(1);

    private MusicPlayerServiceStatus mMusicPlayerServiceStatus;

    final int NOTIFICATION_ID = 1;

    public MusicPlayerService() {
    }

    protected final String TAG =
            getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicTrackRequestImpl;
    }

    @Override
    public void onCreate() {
        super.onCreate();

       // mExecutorService= Executors.newCachedThreadPool();
        mExecutorService= Executors.newSingleThreadExecutor();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        mExecutorService.shutdownNow();
        super.onDestroy();
    }



    private final MusicTrackRequest.Stub mMusicTrackRequestImpl=new MusicTrackRequest.Stub(){
        @Override
        public void playTrack(final Track track, final MusicTrackResults results) throws RemoteException {

            final Runnable playTrackRunnable=new Runnable() {
                @Override
                public void run() {
                    try {

                        initializeMediaPlayer();
                        mSelectedTrack=track;
                        Log.d(TAG, "run: playTrackRunnable entering" );
                        if(mMediaPlayer.isPlaying()){
                            Log.d(TAG, "  playTrackRunnable run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }else {
                            Log.d(TAG, "  playTrackRunnable run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }

                        playMedia(track);
                        //createNotification(mSelectedTrack.getTitle());
                        mMediaPlayerBarrier.await();
                        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PLAYING;
                        results.playing("PLAYING");
                    }catch (Exception e){
                        Log.d(TAG,
                                "MusicTrackRequest.Stub  playTrackRunnable "
                                        + e);
                    }
                }
            };

            if(Utils.runningOnUiThread()){
                mExecutorService.execute(playTrackRunnable);
            }else{
                playTrackRunnable.run();
            }

        }


        @Override
        public void processPlayRequest(final MusicTrackResults callback) throws RemoteException {
            final Runnable processPlayRunnable=new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "run: processPlayRequest entering" );
                        if(mMediaPlayer.isPlaying()){
                            Log.d(TAG, "  processPlayRequest run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }else {
                            Log.d(TAG, "  processPlayRequest run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }
                        mMediaPlayer.start();
                        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PLAYING;
                        callback.playing(MusicPlayerServiceStatus.PLAYING.toString());
                    }catch (Exception e){
                    Log.d(TAG,
                            "MusicTrackRequest.Stub  togglePlayPause "
                                    + e);
                    }
                }
            };
            if(Utils.runningOnUiThread()){
                mExecutorService.execute(processPlayRunnable);
            }else{
                processPlayRunnable.run();
            }
        }

        @Override
        public void processPauseRequest(final MusicTrackResults callback) throws RemoteException {
            final Runnable processPauseRunnable=new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "run: processPauseRequest entering" );
                        if(mMediaPlayer.isPlaying()){
                            Log.d(TAG, "  processPauseRequest run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }else {
                            Log.d(TAG, "  processPauseRequest run: mMediaPlayer.isPlaying()="+mMediaPlayer.isPlaying());
                        }
                        mMediaPlayer.pause();
                        mMusicPlayerServiceStatus=MusicPlayerServiceStatus.PAUSED;
                        callback.paused(MusicPlayerServiceStatus.PAUSED.toString());
                    }catch (Exception e){
                        Log.d(TAG,
                                "MusicTrackRequest.Stub  togglePlayPause "
                                        + e);
                    }
                }
            };
            if(Utils.runningOnUiThread()){
                mExecutorService.execute(processPauseRunnable);
            }else{
                processPauseRunnable.run();
            }
        }
    };

    public static Intent makeIntent(Context context){
        return new Intent(context,MusicPlayerService.class);
    }

    public void pause(){

    }
    public void play(){

    }
    public MusicPlayerServiceStatus getStatus(){
        return mMusicPlayerServiceStatus;
    }
    private PendingIntent makePendingIntent(Context context){
        Intent intent=new Intent(context, FullScreenPlayerActivity.class);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        return pIntent;
    }

    private void initializeMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(mMediaOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mMediaPlayerCompletionListener);
    }

    private MediaPlayer.OnCompletionListener mMediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // mView.get().displayPlayButton();
           // mControlsFragment.displayPauseButton();
        }
    };

    private MediaPlayer.OnPreparedListener mMediaOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
           //playMedia(mSelectedTrack);
            //playPause();
            mMediaPlayerBarrier.countDown();
            mMediaPlayer.start();
        }
    };

    private MusicPlayerServiceStatus playPause(){
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            return MusicPlayerServiceStatus.PAUSED;
        } else {
            mMediaPlayer.start();
            return MusicPlayerServiceStatus.PLAYING;
        }
    }


    public void playMedia(Track track) {


        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(track.getStreamUrl());
            Log.d(TAG, "startToPlayMedia: track.getStreamUrl()"+track.getStreamUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "startToPlayMedia: ex.getLocalizedMessage()=" + ex.getLocalizedMessage());
        }
    }


    void createNotification(String text) {
        Log.d(TAG, "createNotification: "+mSelectedTrack.getThumbUrlMedium());
        try{
           Bitmap bitmap= Picasso.with(getApplicationContext()).load(mSelectedTrack.getThumbUrlLarge()).get();
            Log.d(TAG, "createNotification: "+mSelectedTrack.getThumbUrlMedium());
            NotificationCompat.Action playPauseAction=new NotificationCompat.Action.Builder(R.drawable.ic_pause,"Pause",makePendingIntent(this)).build();
            NotificationCompat.Builder builder=
                    new NotificationCompat.Builder(this)
                            .setLargeIcon(bitmap)
                            .setSmallIcon(android.support.design.R.drawable.navigation_empty_icon)
                            .setContentIntent(makePendingIntent(this))
                            .setContentTitle(text)
                            .addAction(playPauseAction);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }catch (IOException ex){
            Log.e(TAG, "createNotification: "+ex.getMessage());
        }catch (Exception e){
            Log.e(TAG, "createNotification: "+e.getMessage());
        }


    }


}
