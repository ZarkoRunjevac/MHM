package zarkorunjevac.mhm.mhm.model;

import android.content.Context;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.service.HypemApiService;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
public class TrackDownloadModel
        implements MVP.ProvidedMusicModelOps {

    protected final static String TAG =
            TrackDownloadModel.class.getSimpleName();

    private String BASE_URL ="https://api.hypem.com/";



    private HypemApiService mApiService;

    /**
     * A WeakReference used to access methods in the Presenter layer.
     * The WeakReference enables garbage collection.
     */
    private WeakReference<MVP.RequiredPresenterOps> mPresenter;

    @Override
    public void onCreate(MVP.RequiredPresenterOps presenter) {
        mPresenter =
                new WeakReference<>(presenter);


        Retrofit  service = new Retrofit.Builder()
                .baseUrl(BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService =service.create(HypemApiService.class);

    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
    //no-op
    }

    @Override
    public List<Track> downloadPopular(Context context, String mode,int page, int count) throws IOException {
        List<Track> tracks;
        Call<List<Track>> call=mApiService.getPopular(mode,page,count);
        tracks=call.execute().body();
        return tracks;
    }

    @Override
    public List<Track> downloadLatest(Context context, String sort,int page, int count) throws IOException {
        List<Track> tracks;
        Call<List<Track>> call=mApiService.getTracks(page,sort,count);
        tracks=call.execute().body();
        return tracks;
    }
}
