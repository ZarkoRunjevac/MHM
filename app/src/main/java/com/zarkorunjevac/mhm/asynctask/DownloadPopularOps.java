package com.zarkorunjevac.mhm.asynctask;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.zarkorunjevac.mhm.common.GenericAsyncTaskOps;
import com.zarkorunjevac.mhm.model.pojo.Track;
import com.zarkorunjevac.mhm.presenter.TrackPresenter;

/**
 * Created by com.zarkorunjevac.mhm on 26/03/16.
 */
public class DownloadPopularOps implements GenericAsyncTaskOps<String, Void, List<Track>> {
    protected final static String TAG =
            DownloadPopularOps.class.getSimpleName();

    private Context mContext;

    ConcurrentHashMap<String,List<Track>> mDownloadedTracks;
    private TrackPresenter mMusicPresenter;
    private int mPage;
    private int mCount;
    private String mKey;

    public DownloadPopularOps(TrackPresenter musicPresenter, Context context,
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
       mMusicPresenter.onTrackListDownloadComplete(mKey);
    }
}
