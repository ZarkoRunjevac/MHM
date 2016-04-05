package zarkorunjevac.mhm.mhm.asynctask;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTaskOps;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.presenter.TrackPresenter;

/**
 * Created by zarkorunjevac on 26/03/16.
 */
public class DownloadLatestOps implements GenericAsyncTaskOps<String, Void, List<Track>> {

    protected final static String TAG =
            DownloadLatestOps.class.getSimpleName();

    private Context mContext;

    ConcurrentHashMap<String,List<Track>> mDownloadedTracks;
    private TrackPresenter mMusicPresenter;
    private int mPage;
    private int mCount;
    private String mKey;

    public DownloadLatestOps(TrackPresenter musicPresenter, Context context,
                             ConcurrentHashMap<String,List<Track>> downloadedTracks, int page, int count){
        mMusicPresenter=musicPresenter;
        mContext=context;

        mDownloadedTracks=downloadedTracks;
        mPage=page;
        mCount=count;
    }

    @Override
    public List<Track> doInBackground(String... params) {
        List<Track> tracks=null;
        mKey ="latest."+params[0];
        try{
            tracks=mMusicPresenter.getModel().downloadLatest(mContext,params[0],mPage,mCount);
            Log.d(TAG, "doInBackground: download");
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
        mMusicPresenter.onTrackListDownloadComplete(mKey);

    }
}
