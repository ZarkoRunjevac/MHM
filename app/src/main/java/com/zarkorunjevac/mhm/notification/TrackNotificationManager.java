package com.zarkorunjevac.mhm.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.zarkorunjevac.mhm.R;
import com.zarkorunjevac.mhm.common.MusicPlayerServiceStatus;
import com.zarkorunjevac.mhm.presenter.TrackPresenter;
import com.zarkorunjevac.mhm.ui.activity.FullScreenPlayerActivity;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zarko.runjevac on 11/15/2016.
 */

public class TrackNotificationManager extends BroadcastReceiver {
    protected final String TAG = getClass().getSimpleName();

    private static final int NOTIFICATION_ID = 551;
    private static final int REQUEST_CODE = 370;

    public static final String ACTION_PAUSE = "com.zarkorunjevac.mhm.notification.pause";
    public static final String ACTION_PLAY = "com.zarkorunjevac.mhm.notification.play";

    private final TrackPresenter mPresenter;

    private final NotificationManager mNotificationManager;

    private final PendingIntent mPauseIntent;
    private final PendingIntent mPlayIntent;

    private boolean mStarted = false;
    private Bitmap mLargeIcon;
    private Context mContext;

    public TrackNotificationManager(TrackPresenter presenter){
        mPresenter=presenter;

        mNotificationManager = (NotificationManager)mPresenter.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        String pkg = mPresenter.getApplicationContext().getPackageName();
        mPauseIntent=PendingIntent.getBroadcast(mPresenter.getApplicationContext(),REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(pkg),PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent=PendingIntent.getBroadcast(mPresenter.getApplicationContext(),REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg),PendingIntent.FLAG_CANCEL_CURRENT);

        mNotificationManager.cancelAll();


    }

    public void startNotification() {
        if (!mStarted) {

            Notification notification=createNotification();
            if(null!=notification){
                IntentFilter filter=new IntentFilter();
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);

                mPresenter.getApplicationContext().registerReceiver(this,filter);

                mNotificationManager.notify(NOTIFICATION_ID,notification);
                mStarted=true;
            }
        }
    }
    public void updateNotification(){
        Log.d(TAG, "updateNotification: ");
        Notification notification = createNotification();
        if (notification != null) {
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
    public void stopNotification(){
        Log.d(TAG, "stopNotification: ");
        if(mStarted){
            mStarted = false;
           
                mNotificationManager.cancel(NOTIFICATION_ID);
           
        }
    }

    private PendingIntent createContentIntent(){
        Intent openActivity=new Intent(mPresenter.getApplicationContext(), FullScreenPlayerActivity.class);
        openActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(mPresenter.getApplicationContext(),REQUEST_CODE,openActivity,PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Notification createNotification() {
        Log.d(TAG, "createNotification: ");

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(mPresenter.getApplicationContext());
        int playPauseButtonPosition = 0;

        addPlayPauseAction(notificationBuilder);


        final CountDownLatch latch=new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TrackNotificationManager.this.mLargeIcon=Picasso
                                    .with(mPresenter.getApplicationContext())
                                    .load(mPresenter.getSelectedTrack()
                                    .getThumbUrlLarge())
                                     .get();
                    latch.countDown();

                }catch (IOException e){
                    Log.e(TAG, "run: ",e );
                }

            }
        }).start();

        try {
            latch.await();

            notificationBuilder
                    .setSmallIcon(android.support.design.R.drawable.navigation_empty_icon)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    //.setUsesChronometer(true)
                    .setContentIntent(createContentIntent())
                    .setContentTitle(mPresenter.getSelectedTrack().getTitle())
                    .setContentText(mPresenter.getSelectedTrack().getTitle())
                    .setLargeIcon(mLargeIcon);

        }catch (InterruptedException ex){
            Log.e(TAG, "createNotification: ", ex);
        }
        return notificationBuilder.build();
    }



    private void addPlayPauseAction(NotificationCompat.Builder builder) {
        Log.d(TAG, "addPlayPauseAction: ");
        String label;
        int icon;
        PendingIntent intent;
        if(mPresenter.getStatus()== MusicPlayerServiceStatus.PLAYING){
            label=mPresenter.getApplicationContext().getString(R.string.label_pause);
            icon =R.drawable.ic_pause;
            intent=mPauseIntent;
        }else {
            label = mPresenter.getApplicationContext().getString(R.string.label_play);
            icon = R.drawable.ic_play;
            intent = mPlayIntent;
        }
        builder.addAction(new NotificationCompat.Action(icon, label, intent));
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        mContext=context;

        Log.d(TAG, "onReceive: ");

        switch (action) {
            case ACTION_PAUSE:
                mPresenter.pauseMedia();
                Notification notification = createNotification();
                mNotificationManager.notify(NOTIFICATION_ID, notification);
                break;
            case ACTION_PLAY:
               mPresenter.playMedia();
                 notification = createNotification();
                mNotificationManager.notify(NOTIFICATION_ID, notification);
                break;
            default:
                Log.w(TAG, "Unknown intent ignored. Action="+ action);

        }
    }



}
