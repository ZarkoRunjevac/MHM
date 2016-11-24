package com.zarkorunjevac.mhm.common;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zarkorunjevac.mhm.BuildConfig;
import com.zarkorunjevac.mhm.MhmApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by zarko.runjevac on 6/14/2016.
 */
public class RetrofitUtils {

    private static final String CACHE_CONTROL = "Cache-Control";

    protected final  String TAG =
            getClass().getSimpleName();

    public static Retrofit makeRetrofit(String baseUrl){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(makeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    private static OkHttpClient makeOkHttpClient(){

        return new OkHttpClient.Builder()
                .addInterceptor( makeHttpLoggingInterceptor() )
                .addInterceptor( makeOfflineCacheInterceptor() )
                .addNetworkInterceptor( makeCacheInterceptor() )
                .cache( makeCache())
                //.connectTimeout(2, TimeUnit.MINUTES)
                //.readTimeout(2, TimeUnit.MINUTES)
                .build();
    }

    private static Cache makeCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( MhmApplication.getInstance().getCacheDir(), "http-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
            Log.d("RetrofitUtils", "makeCache: "+MhmApplication.getInstance().getCacheDir().toString());
        }
        catch (Exception e)
        {
            Log.e( "RetrofitUtils", "Could not create Cache!" );
        }
        return cache;
    }

    private static HttpLoggingInterceptor makeHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        Log.d("RetrofitUtils", "log: "+message);

                    }
                } );
        httpLoggingInterceptor.setLevel( BuildConfig.DEBUG ? BODY : NONE );
        return httpLoggingInterceptor;
    }

    public static Interceptor makeCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge( 5, TimeUnit.MINUTES )
                        .build();

                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }

    public static Interceptor makeOfflineCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                if ( !MhmApplication.hasNetwork() )
                {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale( 7, TimeUnit.DAYS )
                            .build();

                    request = request.newBuilder()
                            .cacheControl( cacheControl )
                            .build();
                    Log.d("RetrofitUtils", "intercept:  ");
                }

                return chain.proceed( request );
            }
        };
    }

}