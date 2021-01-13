package com.example.binauralbeats;

public class Track {
    String trackId;
    String trackTitle;
    String baseFreq;
    String beatFreq;
    String waveType;

    public Track(){

    }

    public Track(String trackId, String trackTitle, String baseFreq, String beatFreq, String waveType) {
        this.trackId = trackId;
        this.trackTitle = trackTitle;
        this.baseFreq = baseFreq;
        this.beatFreq = beatFreq;
        this.waveType = waveType;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public String getBaseFreq() {
        return baseFreq;
    }

    public String getBeatFreq() {
        return beatFreq;
    }

    public String getWaveType() {
        return waveType;
    }
}
