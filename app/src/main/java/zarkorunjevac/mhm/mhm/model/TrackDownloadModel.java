package zarkorunjevac.mhm.mhm.model;

import android.content.Context;
import android.net.Uri;
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

import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

/**
 * Created by zarkorunjevac on 02/04/16.
 */
public class TrackDownloadModel implements MVP.ProvidedTrackDownloadModelOps {

    protected final static String TAG =
            TrackListDownloadModel.class.getSimpleName();

    private WeakReference<MVP.RequiredTrackPresenterOps> mPresenter;


    @Override
    public void onCreate(MVP.RequiredTrackPresenterOps presenter) {
        mPresenter =
                new WeakReference<>(presenter);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {

    }

    @Override
    public List<String> downloadLinksFromPage(String url) throws IOException {
        List<String> linksOnPage = new ArrayList<String>();

        Document doc = Jsoup.connect(url).get();
        Elements media = doc.select("[src]");

        for (Element src : media) {
            String link = src.attr("abs:src");
            linksOnPage.add(URLDecoder.decode(link, "UTF-8"));
            if (link.contains("api.soundcloud")) {
                Log.d("LatestTacksFragment", "run: " + src.attr("abs:src"));
            }


        }
        return linksOnPage;
    }

    @Override
    public Uri findMusicStremLink(Context context, Track track) {
        return null;
    }
}
