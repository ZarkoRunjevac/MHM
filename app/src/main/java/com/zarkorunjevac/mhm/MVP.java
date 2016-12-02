package com.zarkorunjevac.mhm;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */

import android.content.Context;

import com.zarkorunjevac.mhm.common.FragmentOps;
import com.zarkorunjevac.mhm.common.ModelOps;
import com.zarkorunjevac.mhm.common.TrackListType;
import com.zarkorunjevac.mhm.model.pojo.SoundCloudTrack;
import com.zarkorunjevac.mhm.model.pojo.Track;
import com.zarkorunjevac.mhm.service.MusicTrackResults;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;




public interface MVP {
    /**
     * This interface defines the minimum API needed by the
     * ImagePresenter class in the Presenter layer to interact with
     * DownloadImagesActivity in the View layer.  It extends the
     * ContextView interface so the Model layer can access Context's
     * defined in the View layer.
     */
    public interface RequiredViewOps
            extends com.zarkorunjevac.mhm.common.ContextView {

        void displayProgressBar();

        void dismissProgressBar();

        void reportDownloadFailure(String listName);

        void dispayResults(ConcurrentHashMap<String,List<Track>> trackLists);

    }



    public interface ProvidedTrackListPresenterOps
            extends com.zarkorunjevac.mhm.common.PresenterOps<RequiredViewOps> {

        void startTrackListDownload(List<String> latest, List<String> popular);

        void startTrackDownload(Track track);

        void togglePlayPause();

        void playMedia(Track track);

        Track getSelectedTrack();

        void setTrackListParams(String trackListName,TrackListType trackListType);

        String getTrackListName();

        TrackListType getTrackListType();

        void takePage(int page,TrackListType trackListType, String trackListName);

        void playMedia();

        void pauseMedia();

    }


    public interface RequiredTrackListPresenterOps
            extends com.zarkorunjevac.mhm.common.ContextView {

        void onTrackListDownloadComplete(String listName);

        void onTrackDownloadComplete(String link);
    }




    public interface ProvidedTrackListDownloadModelOps extends ModelOps<RequiredTrackListPresenterOps> {

        List<Track> downloadPopular(Context context, String mode,int page, int count) throws IOException;

        List<Track> downloadLatest(Context context, String sort,int page, int count) throws IOException;

        List<String> downloadLinksFromPage(String url) throws IOException;

        SoundCloudTrack findMusicStreamLink(String id) throws IOException;

        void pauseMedia(MusicTrackResults results);

        void playMedia(MusicTrackResults results);

        void startToPlayMedia(Track track, MusicTrackResults results);


    }

    public interface ProvidedMusicListOps {

        HashMap<String,List<Track>> loadLatestLists( );

        HashMap<String,List<Track>> loadPopularLists();

        void tryToPlayTrack(Track track);

        Track loadTrack();

        void togglePlayPause();


    }

    public interface ProvidedLatestTracksPresenterOps extends FragmentOps{
        void onStreamLinkFound(Track track);
    }

    public interface ProvidedPopularTracksPresenterOps extends FragmentOps{
        void onStreamLinkFound(Track track);
    }

    public interface  ProvidedPlaybackControlsFragmentOps{
        void displayPlayButton();

        void displayPauseButton();

        void initializeViewFields(Track track);
    }


    public interface ProvidedTrackFragmentOps{

        void startTrackListDownload(List<String> latest, List<String> popular);

        void startTrackDownload(Track track);

        void togglePlayPause();

        void playMedia(Track track);

        Track getSelectedTrack();

        void setTrackListParams(String trackListName,TrackListType trackListType);

        String getTrackListName();

        TrackListType getTrackListType();

        void takePage(int page,TrackListType trackListType, String trackListName);

        void playMedia();

        void pauseMedia();

    }



}
