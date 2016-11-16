package com.zarkorunjevac.mhm.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.zarkorunjevac.mhm.model.pojo.SoundCloudTrack;

/**
 * Created by zarko.runjevac on 4/5/2016.
 */
public interface SoundCloudApiService {
    @GET("tracks/{id}")
    Call<SoundCloudTrack> getTrack(@Path("id") String id, @Query("client_id") String client_id);
}
