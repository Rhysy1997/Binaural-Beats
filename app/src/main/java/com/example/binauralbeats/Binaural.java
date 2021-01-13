package com.example.binauralbeats;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Binaural implements BeatsEngine {

    private final int SAMPLE_RATE = 44100;
    private int sampleCount;
    private boolean doRelease;
    private AudioTrack mAudio;
    private boolean isPlaying;

    public Binaural(float baseFrequency, float beatFrequency) {
        int amplitudeMax = Helpers.getAdjustedAmplitudeMax(baseFrequency);

        float freqLeft = baseFrequency - (beatFrequency/2);
        float freqRight = baseFrequency + (beatFrequency/2);

        //period of the sine waves
        int sCountLeft = (int) ((float) SAMPLE_RATE / freqLeft);
        int sCountRight = (int) ((float) SAMPLE_RATE / freqRight);

        sampleCount = Helpers.getLCM(sCountLeft, sCountRight) * 2;
        int buffSize = sampleCount * 4;

        mAudio = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                buffSize, AudioTrack.MODE_STATIC);

        short samples[] = new short[sampleCount];
        int amplitude = amplitudeMax;
        double twopi = 8. * Math.atan(1.);
        double leftPhase = 0.0;
        double rightPhase = 0.0;

        for (int i = 0; i < sampleCount; i = i + 2) {

            samples[i] = (short) (amplitude * Math.sin(leftPhase));
            samples[i + 1] = (short) (amplitude * Math.sin(rightPhase));

            if (i/2 % sCountLeft == 0) {
                leftPhase = 0.0;
            }
            leftPhase += twopi * freqLeft / SAMPLE_RATE;
            if (i/2 % sCountRight == 0) {
                rightPhase = 0.0;
            }
            rightPhase += twopi * freqRight / SAMPLE_RATE;
        }
        mAudio.write(samples, 0, sampleCount);
        mAudio.setStereoVolume(0.0f, 0.0f);
        Helpers.napThread();
    }

    public void release() {
        doRelease = true;
        stop();
    }

    public void start() {
        mAudio.reloadStaticData();
        mAudio.setLoopPoints(0, sampleCount / 2, -1);
        isPlaying = true;
        mAudio.play();
        Helpers.napThread();
        mAudio.setStereoVolume(1f, 1f);
    }

    public void stop() {

        mAudio.setStereoVolume(0.0f, 0.0f);
        Helpers.napThread();
        mAudio.stop();
        isPlaying = false;
        if (doRelease) {
            mAudio.flush();
            mAudio.release();
        }
    }

    public boolean getIsPlaying() { return isPlaying; }

    //control wave volume with 0.0 meaning silence, 1.0 is max gain without distortion
    public void setVolume(float volume){
        float volumeFloat;
        //seekbar max is int 100 so divide by 100
        volumeFloat = (volume / 100f);
        mAudio.setVolume(volumeFloat);
    }

    public void play() {
        mAudio.reloadStaticData();
        mAudio.setLoopPoints(0, sampleCount / 2, -1);
        isPlaying = true;
        mAudio.play();
        Helpers.napThread();
    }

    public void pause() {
        mAudio.setStereoVolume(0.0f, 0.0f);
        Helpers.napThread();
        mAudio.stop();
        isPlaying = false;
    }

}
