package zarkorunjevac.mhm.mhm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zarkorunjevac.mhm.mhm.model.pojo.Track;

public class MusicPlayerService extends Service {

    private ExecutorService mExecutorService;

    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicTrackRequestImpl;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mExecutorService= Executors.newCachedThreadPool();
    }

    @Override
    public void onDestroy() {
        mExecutorService.shutdownNow();
        super.onDestroy();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context,MusicPlayerService.class);
    }

    private final MusicTrackRequest.Stub mMusicTrackRequestImpl=new MusicTrackRequest.Stub(){
        @Override
        public void playTrack(Track track, MusicTrackResults results) throws RemoteException {

        }

        @Override
        public void togglePlayPause(MusicTrackResults results) throws RemoteException {

        }
    };


}
