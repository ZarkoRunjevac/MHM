package zarkorunjevac.mhm.mhm;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import zarkorunjevac.mhm.mhm.common.ContextView;
import zarkorunjevac.mhm.mhm.common.ModelOps;
import zarkorunjevac.mhm.mhm.common.PresenterOps;
import zarkorunjevac.mhm.mhm.model.pojo.SoundCloudTrack;
import zarkorunjevac.mhm.mhm.model.pojo.Track;


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
        /**
         * Make the ProgressBar visible.
         */
        void displayProgressBar();

        /**
         * Make the ProgressBar invisible.
         */
        void dismissProgressBar();

        void reportDownloadFailure(String listName);

        void dispayResults(ConcurrentHashMap<String,List<Track>> trackLists);

        void returnTrackLink(String link);

    }

    /**
     * This interface defines the minimum public API provided by the
     * ImagePresenter class in the Presenter layer to the
     * DownloadImagesActivity in the View layer.  It extends the
     * PresenterOps interface, which is instantiated by the
     * MVP.RequiredViewOps interface used to define the parameter
     * that's passed to the onConfigurationChange() method.
     */
    public interface ProvidedPresenterOps
            extends PresenterOps<MVP.RequiredViewOps> {
        /**
         * Get the list of URLs.
         */
        ArrayList<Uri> getUrlList();

        /**
         * Perform the download and filter processing.
         */
        void startProcessing();



        /**
         * Delete all the downloaded images.
         */
        void deleteDownloadedImages();
    }

    public interface ProvidedTrackListPresenterOps
            extends PresenterOps<MVP.RequiredViewOps> {

        void startTrackListDownload(List<String> latest, List<String> popular);

        void startTrackDownload(Track track);

    }

    /**
     * This interface defines the minimum API needed by the ImageModel
     * class in the Model layer to interact with ImagePresenter class
     * in the Presenter layer.  It extends the ContextView interface
     * so the Model layer can access Context's defined in the View
     * layer.
     */
    public interface RequiredTrackListPresenterOps
            extends ContextView {


        void onTrackListDownloadComplete(String listName);
        void onTrackDownloadComplete(String link);
    }

    /**
     * This interface defines the minimum public API provided by the
     * ImageModel class in the Model layer to the ImagePresenter class
     * in the Presenter layer.  It extends the ModelOps interface,
     * which is parameterized by the MVP.RequiredPresenterOps
     * interface used to define the argument passed to the
     * onConfigurationChange() method.
     */
    public interface ProvidedModelOps
            extends ModelOps<RequiredTrackListPresenterOps> {
        /**
         * Download the image located at the provided Internet url
         * using the URL class, store it on the android file system
         * using a FileOutputStream, and return the path to the image
         * file on disk.
         *
         * @param context
         *          The context in which to write the file.
         * @param url
         *          The URL of the image to download.
         * @param directoryPathname
         *          Pathname of the directory to write the file.
         *
         * @return
         *        Absolute path to the downloaded image file on the file
         *        system.
         */
        Uri downloadImage(Context context,
                          Uri url,
                          Uri directoryPathname);


    }

    public interface ProvidedTrackListDownloadModelOps extends ModelOps<RequiredTrackListPresenterOps> {

        List<Track> downloadPopular(Context context, String mode,int page, int count) throws IOException;

        List<Track> downloadLatest(Context context, String sort,int page, int count) throws IOException;

        List<String> downloadLinksFromPage(String url) throws IOException;

        SoundCloudTrack findMusicStreamLink(String id) throws IOException;
    }

    public interface ProvidedMusicListActivityOps{

        HashMap<String,List<Track>> loadLatestLists( );

        HashMap<String,List<Track>> loadPopularLists();

        void getStreamUrl(Track track);

        String returnStreamUrl(String link);
    }



}
