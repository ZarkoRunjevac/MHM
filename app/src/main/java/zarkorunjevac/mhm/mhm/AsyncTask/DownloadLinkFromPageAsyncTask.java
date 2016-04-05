package zarkorunjevac.mhm.mhm.asynctask;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTask;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

/**
 * Created by zarkorunjevac on 03/04/16.
 */
public class DownloadLinkFromPageAsyncTask extends GenericAsyncTask<Track,Void, String,DownloadLinkFromPageOps> {
    public DownloadLinkFromPageAsyncTask(DownloadLinkFromPageOps ops){
        super(ops);
    }
}
