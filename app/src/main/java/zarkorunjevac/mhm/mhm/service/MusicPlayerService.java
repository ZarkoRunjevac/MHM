package zarkorunjevac.mhm.mhm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zarkorunjevac.mhm.mhm.common.MusicPlayerServiceStatus;
import zarkorunjevac.mhm.mhm.common.Utils;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

public class MusicPlayerService extends Service {

    private ExecutorService mExecutorService;

    private MediaPlayer mMediaPlayer=null;

    private Track mSelectedTrack;

    private NotificationManager mNotificationManager;

    private Notification mNotification = null;

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

        mExecutorService= Executors.newCachedThreadPool();

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
                        playMedia(track);
                        Log.d(TAG, "run: mMediaPlayer.isPlaying()"+mMediaPlayer.isPlaying());
                        Log.d(TAG, "playTrackRunnable ");
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
        public void togglePlayPause(final MusicTrackResults callback) throws RemoteException {
            final Runnable togglePlayPauseRunnable=new Runnable() {
                @Override
                public void run() {
                    try {
                        MusicPlayerServiceStatus status=playPause();
                        Log.d(TAG, "MusicTrackRequest.Stub  togglePlayPause : PLAYING" );
                        if(status==MusicPlayerServiceStatus.PAUSED){

                            callback.paused(MusicPlayerServiceStatus.PAUSED.toString());
                        }else{
                            Log.d(TAG, "MusicTrackRequest.Stub  togglePlayPause : PLAYING" );
                            callback.playing(MusicPlayerServiceStatus.PLAYING.toString());
                        }


                    }catch (Exception e){
                        Log.d(TAG,
                                "MusicTrackRequest.Stub  togglePlayPause "
                                        + e);
                    }
                }
            };
            if(Utils.runningOnUiThread()){
                mExecutorService.execute(togglePlayPauseRunnable);
            }else{
                togglePlayPauseRunnable.run();
            }
        }
    };

    public static Intent makeIntent(Context context){
        return new Intent(context,MusicPlayerService.class);
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
            playPause();
            updateNotification(mSelectedTrack.getTitle());
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
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(track.getStreamUrl());
            Log.d(TAG, "playMedia: track.getStreamUrl()"+track.getStreamUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "playMedia: ex.getLocalizedMessage()=" + ex.getLocalizedMessage());
        }
    }


    void updateNotification(String text) {

        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.support.design.R.drawable.navigation_empty_icon)
                .setContentTitle(text);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
