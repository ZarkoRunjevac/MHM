// MusicTrackRequest.aidl
package zarkorunjevac.mhm.mhm.service;
import zarkorunjevac.mhm.mhm.service.MusicTrackResponse;

// Declare any non-default types here with import statements

interface MusicTrackRequest {

    oneway void playTrack(in Track track,
                          in MusicTrackResponse results);
}
