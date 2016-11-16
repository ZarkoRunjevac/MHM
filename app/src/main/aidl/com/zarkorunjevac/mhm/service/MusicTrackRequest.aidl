// MusicTrackRequest.aidl
package com.zarkorunjevac.mhm.service;
import com.zarkorunjevac.mhm.service.MusicTrackResults;
import com.zarkorunjevac.mhm.model.pojo.Track;



interface MusicTrackRequest {

    oneway void playTrack(in Track track,
                          in MusicTrackResults results);


    oneway void processPlayRequest(in MusicTrackResults results);
    oneway void processPauseRequest(in MusicTrackResults results);

}
