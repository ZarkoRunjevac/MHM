package zarkorunjevac.mhm.mhm.AsyncTask;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTaskOps;
import zarkorunjevac.mhm.mhm.model.Track;
import zarkorunjevac.mhm.mhm.presenter.MusicPresenter;

/**
 * Created by zarkorunjevac on 26/03/16.
 */
public class DownloadPopularOps implements GenericAsyncTaskOps<String, Void, List<Track>> {
    protected final static String TAG =
            DownloadPopularOps.class.getSimpleName();

    private Context mContext;
    private CountDownLatch mExitBarrier;
    ConcurrentHashMap<String,List<Track>> mDownloadedTracks;
    private MusicPresenter mMusicPresenter;
    private int mPage;
    private int mCount;
    private String mKey;

    public DownloadPopularOps(MusicPresenter musicPresenter, Context context, CountDownLatch exitBarrier,
                              ConcurrentHashMap<String,List<Track>> downloadedTracks,int page, int count){
        mMusicPresenter=musicPresenter;
        mContext=context;
        mExitBarrier=exitBarrier;
        mDownloadedTracks=downloadedTracks;
        mPage=page;
        mCount=count;
    }


    @Override
    public List<Track> doInBackground(String... params) {
        List<Track> tracks=null;
        mKey ="popular."+params[0];
        try{
            tracks=mMusicPresenter.getModel().downloadPopular(mContext,params[0],mPage,mCount);
        }
        catch (IOException e){
            Log.d(TAG, "doInBackground: "+e.getLocalizedMessage());
        }
        finally {
            return tracks;
        }

    }

    @Override
    public void onPostExecute(List<Track> tracks) {
        if(null!=tracks) mDownloadedTracks.put(mKey,tracks);
        mExitBarrier.countDown();
    }
}
