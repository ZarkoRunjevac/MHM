// MusicTrackResponse.aidl
package com.zarkorunjevac.mhm.service;
import com.zarkorunjevac.mhm.model.pojo.Track;


// Declare any non-default types here with import statements

interface MusicTrackResults {

    oneway void playing(in String isPlaying);
    oneway void paused(in String isPaused);
    oneway void stopped(in String isStopped);


}
