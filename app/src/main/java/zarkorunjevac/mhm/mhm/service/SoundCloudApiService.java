package zarkorunjevac.mhm.mhm.service;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import zarkorunjevac.mhm.mhm.model.pojo.SoundCloudTrack;

/**
 * Created by zarko.runjevac on 4/5/2016.
 */
public interface SoundCloudApiService {
    @GET("tracks/{id}")
    Call<SoundCloudTrack> getTrack(@Path("id") String id, @Query("client_id") String client_id);
}
