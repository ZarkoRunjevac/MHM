package com.zarkorunjevac.mhm.asynctask;

import java.util.List;

import com.zarkorunjevac.mhm.common.GenericAsyncTask;
import com.zarkorunjevac.mhm.model.pojo.Track;

/**
 * Created by com.zarkorunjevac.mhm on 26/03/16.
 */
public class DownloadPopularAsyncTask extends GenericAsyncTask<String, Void, List<Track>,DownloadPopularOps> {
    public DownloadPopularAsyncTask(DownloadPopularOps ops) {
        super(ops);
    }
}
