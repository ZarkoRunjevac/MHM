// MusicTrackRequest.aidl
package zarkorunjevac.mhm.mhm.service;
import zarkorunjevac.mhm.mhm.service.MusicTrackResults;
import zarkorunjevac.mhm.mhm.model.pojo.Track;



interface MusicTrackRequest {

    oneway void playTrack(in Track track,
                          in MusicTrackResults results);

    oneway void togglePlayPause(in MusicTrackResults results);
}
