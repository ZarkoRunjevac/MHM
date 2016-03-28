package zarkorunjevac.mhm.mhm.AsyncTask;

import java.util.List;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTask;
import zarkorunjevac.mhm.mhm.model.Track;

/**
 * Created by zarkorunjevac on 26/03/16.
 */
public class DownloadLatestAsyncTask extends GenericAsyncTask<String,Void,List<Track>,DownloadLatestOps> {

    public DownloadLatestAsyncTask(DownloadLatestOps ops){
        super(ops);
    }

}
