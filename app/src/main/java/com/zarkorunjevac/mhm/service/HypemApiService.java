package com.zarkorunjevac.mhm.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.zarkorunjevac.mhm.model.pojo.Blog;
import com.zarkorunjevac.mhm.model.pojo.Track;

/**
 * Created by zarko.runjevac on 3/22/2016.
 */
public interface HypemApiService {
    @GET("v2/blogs")
    Call<List<Blog>> getBlogs(@Query("hydrate") Boolean hydrate, @Query("page") int page, @Query("count") int count);

    @GET("v2/tracks")
    Call<List<Track>> getTracks(@Query("page") int page,@Query("mode") String mode, @Query("count")int count);

    @GET("v2/popular")
    Call<List<Track>> getPopular(@Query("mode") String mode,@Query("page") int page, @Query("count")int count);

}
