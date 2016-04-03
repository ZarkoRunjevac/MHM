package zarkorunjevac.mhm.mhm.AsyncTask;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTaskOps;
import zarkorunjevac.mhm.mhm.presenter.TrackPresenter;

/**
 * Created by zarkorunjevac on 03/04/16.
 */
public class DownloadLinkFromPageOps implements GenericAsyncTaskOps<String, Void, List<String>> {

    private TrackPresenter mTrackPresenter;

    public DownloadLinkFromPageOps(TrackPresenter trackPresenter){
        mTrackPresenter=trackPresenter;
    }

    @Override
    public List<String> doInBackground(String... params) {
        String url=params[0];
        List<String> links=null;
        try{
           links= mTrackPresenter.getModel().downloadLinksFromPage(url);

        }catch (IOException e){

        }
        return links;
    }

    @Override
    public void onPostExecute(List<String> strings) {
        mTrackPresenter.onProcessingComplete(strings);
    }
}
