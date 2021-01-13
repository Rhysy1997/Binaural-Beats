package com.example.binauralbeats;

public interface BeatsEngine {
    void start();
    void stop();
    void release();
    void setVolume(float volume);
    boolean getIsPlaying();
    void play();
    void pause();
}
