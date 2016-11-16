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
            Log.d(TAG, "doInBackground: "+e.getMessage());
        }
        finally {
            return tracks;
        }
    }

    @Override
    public void onPostExecute(List<Track> tracks) {
        Log.d(TAG, "onPostExecute: enter");
        if(null!=tracks){
            Log.d(TAG, "onPostExecute: tracks.size="+tracks.size());
            mDownloadedTracks.put(mKey, tracks);
        }
        mMusicPresenter.onTrackListDownloadComplete(mKey);

    }
}
