package com.zarkorunjevac.mhm.asynctask;

import com.zarkorunjevac.mhm.common.GenericAsyncTask;
import com.zarkorunjevac.mhm.model.pojo.Track;

/**
 * Created by com.zarkorunjevac.mhm on 03/04/16.
 */
public class DownloadLinkFromPageAsyncTask extends GenericAsyncTask<Track,Void, String,DownloadLinkFromPageOps> {
    public DownloadLinkFromPageAsyncTask(DownloadLinkFromPageOps ops){
        super(ops);
    }
}
