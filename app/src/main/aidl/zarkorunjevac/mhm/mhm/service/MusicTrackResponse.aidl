// MusicTrackResponse.aidl
package zarkorunjevac.mhm.mhm.service;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

// Declare any non-default types here with import statements

interface MusicTrackResponse {
    oneway void sendError(in String reason);
}
