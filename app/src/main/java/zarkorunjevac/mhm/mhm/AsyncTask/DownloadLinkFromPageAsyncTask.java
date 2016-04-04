package zarkorunjevac.mhm.mhm.asynctask;

import java.util.List;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTask;

/**
 * Created by zarkorunjevac on 03/04/16.
 */
public class DownloadLinkFromPageAsyncTask extends GenericAsyncTask<String,Void, List<String>,DownloadLinkFromPageOps> {
    public DownloadLinkFromPageAsyncTask(DownloadLinkFromPageOps ops){
        super(ops);
    }
}
