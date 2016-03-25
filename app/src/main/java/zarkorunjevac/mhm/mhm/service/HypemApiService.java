package zarkorunjevac.mhm.mhm.service;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import zarkorunjevac.mhm.mhm.model.Blog;

/**
 * Created by zarko.runjevac on 3/22/2016.
 */
public interface HypemApiService {
    @GET("v2/blogs")
    Call<List<Blog>> getBlogs(@Query("hydrate") Boolean hydrate, @Query("page") int page, @Query("count") int count);
}
