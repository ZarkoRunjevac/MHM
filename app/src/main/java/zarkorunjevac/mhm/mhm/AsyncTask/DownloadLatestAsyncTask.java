package zarkorunjevac.mhm.mhm.asynctask;

import java.util.List;

import zarkorunjevac.mhm.mhm.common.GenericAsyncTask;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

/**
 * Created by zarkorunjevac on 26/03/16.
 */
public class DownloadLatestAsyncTask extends GenericAsyncTask<String,Void,List<Track>,DownloadLatestOps> {

    public DownloadLatestAsyncTask(DownloadLatestOps ops){
        super(ops);
    }

}
