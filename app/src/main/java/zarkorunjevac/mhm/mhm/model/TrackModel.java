package zarkorunjevac.mhm.mhm.model;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.Config;
import zarkorunjevac.mhm.mhm.common.GenericServiceConnection;
import zarkorunjevac.mhm.mhm.common.MusicPlayerServiceStatus;
import zarkorunjevac.mhm.mhm.common.RetrofitUtils;
import zarkorunjevac.mhm.mhm.model.pojo.SoundCloudTrack;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.service.HypemApiService;
import zarkorunjevac.mhm.mhm.service.MusicPlayerService;
import zarkorunjevac.mhm.mhm.service.MusicTrackRequest;
import zarkorunjevac.mhm.mhm.service.MusicTrackResults;
import zarkorunjevac.mhm.mhm.service.SoundCloudApiService;
//import zarkorunjevac.mhm.mhm.service.


/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class TrackModel
        implements MVP.ProvidedTrackListDownloadModelOps {

    protected final static String TAG =
            TrackModel.class.getSimpleName();

    private String BASE_URL_HYPEM ="https://api.hypem.com/";

    private String BASE_URL_SOUNDCLOUD="http://api.soundcloud.com/";


    private HypemApiService mHypemApiService;

    private SoundCloudApiService mSoundCloudApiService;

    private GenericServiceConnection<MusicTrackRequest> mServiceConnectionAsync;



    /**
     * A WeakReference used to access methods in the Presenter layer.
     * The WeakReference enables garbage collection.
     */
    private WeakReference<MVP.RequiredTrackListPresenterOps> mPresenter;

    @Override
    public void onCreate(MVP.RequiredTrackListPresenterOps presenter) {
        mPresenter =
                new WeakReference<>(presenter);

        mHypemApiService =makeHypemService();


        mSoundCloudApiService=makeSoundCloudService();

        mServiceConnectionAsync=
                new GenericServiceConnection<>(MusicTrackRequest.class);

        bindServices();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {

        if (isChangingConfigurations)
            Log.d(TAG,
                    "just a configuration change - unbindService() not called");
        else
            // Unbind from the Services only if onDestroy() is not
            // triggered by a runtime configuration change.
            unbindServices();
    }

    @Override
    public List<Track> downloadPopular(Context context, String mode,int page, int count) throws IOException {
        List<Track> tracks;
        Call<List<Track>> call= mHypemApiService.getPopular(mode,page,count);
        tracks=call.execute().body();
        return tracks;
    }

    @Override
    public List<Track> downloadLatest(Context context, String mode,int page, int count) throws IOException {
        List<Track> tracks;
        Call<List<Track>> call= mHypemApiService.getTracks(page, mode, count);
        tracks=call.execute().body();
        return tracks;
    }

    @Override
    public List<String> downloadLinksFromPage(String url) throws IOException {
        List<String> linksOnPage = new ArrayList<String>();

        Document doc = Jsoup.connect(url).get();
        Elements media_src = doc.select("[src]");
        //Elements media_data_source=doc.select("")

        for (Element src : media_src) {
            String link = URLDecoder.decode(src.attr("abs:src"), "UTF-8");

            if (link.contains("api.soundcloud")) {
                linksOnPage.add(link);
                Log.d(TAG, "run: " + src.attr("abs:src"));
            }


        }
        return linksOnPage;
    }

    @Override
    public SoundCloudTrack findMusicStreamLink(String id) throws IOException{

        SoundCloudTrack track;
        Call<SoundCloudTrack> call=mSoundCloudApiService.getTrack(id, Config.CLIENT_ID);
        track=call.execute().body();
        return track;
    }

    private HypemApiService makeHypemService(){

        Retrofit  hypemService = RetrofitUtils.makeRetrofit(BASE_URL_HYPEM);
        return hypemService.create(HypemApiService.class);
    }

    private SoundCloudApiService makeSoundCloudService(){
        Retrofit  soundcloudService = RetrofitUtils.makeRetrofit(BASE_URL_SOUNDCLOUD);

        return soundcloudService.create(SoundCloudApiService.class);
    }

    @Override
    public void togglePlayPause(MusicTrackResults results) {
        final MusicTrackRequest musicTrackRequest=
                mServiceConnectionAsync.getInterface();
        MusicPlayerServiceStatus status;
        if(null!= musicTrackRequest){
            try {
                musicTrackRequest.togglePlayPause(new MusicTrackResultsImpl(results));
            }catch (RemoteException e){
                Log.e(TAG,
                        "togglePlayPause RemoteException:"
                                + e.getMessage());
            }

        }else{
            Log.d(TAG,
                    "musicTrackRequest was null.");
        }

    }

    @Override
    public void playMedia(Track track,MusicTrackResults results) {
        final MusicTrackRequest musicTrackRequest=
                mServiceConnectionAsync.getInterface();

        if(null!= musicTrackRequest){
            try {
              musicTrackRequest.playTrack(track,new MusicTrackResultsImpl(results));
            }catch (RemoteException e){
                Log.e(TAG,
                        "togglePlayPause RemoteException:"
                                + e.getMessage());
            }

        }else{
            Log.d(TAG,
                    "musicTrackRequest was null.");
        }
    }

    private void bindServices() {
        Log.d(TAG,
                "calling bindService()");
        if(null==mServiceConnectionAsync.getInterface()){
            mPresenter.get()
                    .getApplicationContext()
                    .bindService
                            (MusicPlayerService.makeIntent(mPresenter.get().getActivityContext()),
                             mServiceConnectionAsync,
                             Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindServices() {
        Log.d(TAG,
                "calling unbindService()");

        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            mPresenter.get()
                    .getApplicationContext()
                    .unbindService
                            (mServiceConnectionAsync);
    }

    private static class MusicTrackResultsImpl extends MusicTrackResults.Stub{

        private WeakReference<MusicTrackResults> mMusicTrackResults;
        public MusicTrackResultsImpl(MusicTrackResults musicTrackResults){
            mMusicTrackResults=new WeakReference<MusicTrackResults>(musicTrackResults);
        }

        @Override
        public void playing(String isPlaying) throws RemoteException {

        }

        @Override
        public void paused(String isPaused) throws RemoteException {

        }

        @Override
        public void stopped(String isStopped) throws RemoteException {

        }
    }
}
