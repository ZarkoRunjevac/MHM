package zarkorunjevac.mhm.mhm;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import zarkorunjevac.mhm.mhm.common.ContextView;
import zarkorunjevac.mhm.mhm.common.FragmentOps;
import zarkorunjevac.mhm.mhm.common.ModelOps;
import zarkorunjevac.mhm.mhm.common.PresenterOps;
import zarkorunjevac.mhm.mhm.common.TrackListType;
import zarkorunjevac.mhm.mhm.model.pojo.SoundCloudTrack;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.service.MusicTrackResults;


public interface MVP {
    /**
     * This interface defines the minimum API needed by the
     * ImagePresenter class in the Presenter layer to interact with
     * DownloadImagesActivity in the View layer.  It extends the
     * ContextView interface so the Model layer can access Context's
     * defined in the View layer.
     */
    public interface RequiredViewOps
            extends ContextView {

        void displayProgressBar();

        void dismissProgressBar();

        void reportDownloadFailure(String listName);

        void dispayResults(ConcurrentHashMap<String,List<Track>> trackLists);

    }



    public interface ProvidedTrackListPresenterOps
            extends PresenterOps<MVP.RequiredViewOps> {

        void startTrackListDownload(List<String> latest, List<String> popular);

        void startTrackDownload(Track track);

        void togglePlayPause();

        void playMedia(Track track);

        Track getSelectedTrack();

        void setTrackListParams(String trackListName,TrackListType trackListType);

        String getTrackListName();

        TrackListType getTrackListType();

        void takePage(int page,TrackListType trackListType, String trackListName);

    }


    public interface RequiredTrackListPresenterOps
            extends ContextView {

        void onTrackListDownloadComplete(String listName);

        void onTrackDownloadComplete(String link);
    }




    public interface ProvidedTrackListDownloadModelOps extends ModelOps<RequiredTrackListPresenterOps> {

        List<Track> downloadPopular(Context context, String mode,int page, int count) throws IOException;

        List<Track> downloadLatest(Context context, String sort,int page, int count) throws IOException;

        List<String> downloadLinksFromPage(String url) throws IOException;

        SoundCloudTrack findMusicStreamLink(String id) throws IOException;

        void togglePlayPause(MusicTrackResults results);

        void playMedia(Track track,MusicTrackResults results);


    }

    public interface ProvidedMusicListActivityOps{

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
}
