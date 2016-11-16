package com.zarkorunjevac.mhm.common;

/**
 * Created by zarko.runjevac on 3/24/2016.
 */
/**
 * The base interface that an operations ("Ops") class can implement
 * so that it can be notified automatically by the GenericAsyncTask
 * framework during the AsyncTask processing.
 */
public interface GenericAsyncTaskOps<Params, Progress, Result> {
    /**
     * Called in the UI thread prior to running doInBackground() in a
     * background thread.
     */
    // @@ Commented out until Android supports default methods in interfaces..
    // default void onPreExecute() {}

    /**
     * Called in a background thread to process the @a params.
     */
    @SuppressWarnings("unchecked")
    Result doInBackground(Params... params);

    /**
     * Called in the UI thread to process the @a result.
     */
    void onPostExecute(Result result);
}

