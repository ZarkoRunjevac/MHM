package zarkorunjevac.mhm.mhm.asynctask;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTaskOps;
import zarkorunjevac.mhm.mhm.model.pojo.SoundCloudTrack;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.presenter.TrackPresenter;

/**
 * Created by zarkorunjevac on 03/04/16.
 */
public class DownloadLinkFromPageOps implements GenericAsyncTaskOps<Track, Void, String> {

    protected final static String TAG =
            DownloadLinkFromPageOps.class.getSimpleName();

    private TrackPresenter mTrackPresenter;

    public DownloadLinkFromPageOps(TrackPresenter trackPresenter){
        mTrackPresenter=trackPresenter;
    }

    @Override
    public String doInBackground(Track... params) {
        //String url=params[0];
        Track track=params[0];
        List<String> links=null;
        //String link=null;
        try{
           links= mTrackPresenter.getModel().downloadLinksFromPage(track.getPosturl());

        }catch (IOException e){

        }
        //check for soundcloud
        if(null!=links){
            for (String link:links){
                String id=getTrackId(link);
                if(null!=id ){
                    SoundCloudTrack soundCloudTrack;
                    try {

                        soundCloudTrack=mTrackPresenter.getModel().findMusicStreamLink(id);
                        if(soundCloudTrack.getTitle().equals(track.getTitle()) || soundCloudTrack.getTitle().contains(track.getTitle())){
                            return soundCloudTrack.getUri();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (NullPointerException ex){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(String link) {
        mTrackPresenter.onTrackDownloadComplete(link);
    }

    private String getTrackId(String link){
        String pattern="/tracks/(\\d+)";
        Pattern r = Pattern.compile(pattern);
        String trackLink=null;

        Log.d("LatestTacksFragment", "run: " + link);
        Matcher m = r.matcher(link);
        while(m.find()){
               Log.d("LatestTacksFragment", "run: "+m.group());
               trackLink=m.group();
        }
        if(null!=trackLink){
            String[] parts=trackLink.split(Pattern.quote("/"));

            return parts[2];
        }

        return null;
    }
}
