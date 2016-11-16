package com.zarkorunjevac.mhm.asynctask;

import java.util.List;

import com.zarkorunjevac.mhm.common.GenericAsyncTask;
import com.zarkorunjevac.mhm.model.pojo.Track;

/**
 * Created by com.zarkorunjevac.mhm on 26/03/16.
 */
public class DownloadLatestAsyncTask extends GenericAsyncTask<String,Void,List<Track>,DownloadLatestOps> {

    public DownloadLatestAsyncTask(DownloadLatestOps ops){
        super(ops);
    }

}
