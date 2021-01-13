package com.example.binauralbeats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.example.binauralbeats.LibraryActivity.EXTRA_TITLE;
import static com.example.binauralbeats.LibraryActivity.EXTRA_BASE;
import static com.example.binauralbeats.LibraryActivity.EXTRA_BEAT;

public class PlayActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    //buttons
    private Button btnPlay;
    public  Button btnTimer;
    private Button btnAddMusic;
    private TextView mTxtViewPlaying;

    //wave
    private BeatsEngine wave;
    float baseFrequency, beatFrequency;
    SeekBar seekbarWaveVolume;

    //music
    public static MediaPlayer player;
    SeekBar seekbarMusicVolume;

    //timer
    private CountDownTimer countDownTimer;
    private long mtimeLeftInMillis; //10 mins etc
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //animated background start
        ConstraintLayout constraintLayout = findViewById(R.id.layout_play);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //passing values from recycler view library
        Intent intent = getIntent();
        String trackTitle = intent.getStringExtra(EXTRA_TITLE);
        String baseFreq = intent.getStringExtra(EXTRA_BASE);
        String beatFreq = intent.getStringExtra(EXTRA_BEAT);

        TextView textViewTitle = findViewById(R.id.textView_play_title);
        textViewTitle.setText(trackTitle);

        btnPlay = findViewById(R.id.button_play_playBtn);
        btnAddMusic = findViewById(R.id.button_add_music);

        btnAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PlayActivity.this, btnAddMusic);
                popup.getMenuInflater().inflate(R.menu.popup_menu_music, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.item1:
                                stopMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item2:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.healing_water);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item3:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.in_the_light);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item4:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.nostalgia);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item5:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.quiet_time);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item6:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.sad_winds);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                            case R.id.item7:
                                stopMusic();
                                player = MediaPlayer.create(PlayActivity.this, R.raw.deep_meditation);
                                playMusic();
                                mTxtViewPlaying.setText(item.getTitle());
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        //timer
        btnTimer = findViewById(R.id.button_timer);


        baseFrequency = Float.parseFloat(baseFreq);
        beatFrequency = Float.parseFloat(beatFreq);
        //initialise new wave with frequencies
        wave = new Binaural(baseFrequency, beatFrequency);

        //wave volume control
        IntializeVolumeControls();

        mTxtViewPlaying = findViewById(R.id.textView_background_playing);
    }

    public void playWave(View view){
        if(!wave.getIsPlaying()){
            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_filled);
            wave.setVolume(seekbarWaveVolume.getProgress());
            wave.play();
        }else{
            btnPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
            wave.pause();
        }
    }

    private void IntializeVolumeControls() {
        seekbarWaveVolume = findViewById(R.id.seekBar_wave_volume);
        seekbarWaveVolume.setMax(100);
        seekbarWaveVolume.setMin(0);
        seekbarWaveVolume.setProgress(5);
        seekbarWaveVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wave.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbarMusicVolume = findViewById(R.id.seekBar_music_volume);
        seekbarMusicVolume.setMax(100);
        seekbarMusicVolume.setMin(0);
        seekbarMusicVolume.setProgress(10);
        seekbarMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null){
                    setMusicVolume(progress, progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wave.getIsPlaying()){
            if (wave != null) {
                wave.release();
            }
        }

        stopPlayer();
        stopTimer();
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    */

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //Music methods
    public void playMusic(){
        setMusicVolume(seekbarMusicVolume.getProgress(), seekbarMusicVolume.getProgress());

        if(player != null){
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.start();
                }
            });
            player.start();
        }
        //start was here b4
    }

    public void pauseMusic(){
        if (player != null) {
            player.pause();
        }
    }

    public void stopMusic(){
        stopPlayer();
    }

    private void stopPlayer(){
        if(player != null){
            player.release();
            player = null;
            //Toast.makeText(this, "MediaPlayer released.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMusicVolume(float left, float right){
        float volumeLeft, volumeRight;
        //seekbar max is int 100 so divide by 100
        volumeLeft = (left / 100f);
        volumeRight = (right / 100f);
        player.setVolume(volumeLeft,volumeRight);
    }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //show timer popup
    public void showPopup(View view){
        PopupMenu popup = new PopupMenu(this, view, Gravity.CENTER);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_timer);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int time;
        switch(item.getItemId()){
            case R.id.item1:
                //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                stopTimer();
                btnTimer.setText("Timer");
                return true;
            case R.id.item2:
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                time = 300000; //5 mins
                stopTimer();
                setTime(time);
                startTimer();
                return true;
            case R.id.item3:
                //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                time = 600000; //10 mins
                stopTimer();
                setTime(time);
                startTimer();
                return true;
            case R.id.item4:
                //Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                time = 900000; //15 mins
                stopTimer();
                setTime(time);
                startTimer();
                return true;
            case R.id.item5:
                //Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                time = 1200000; //20 mins
                stopTimer();
                setTime(time);
                startTimer();
                return true;
            case R.id.item6:
                //Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                time = 1500000; //25 mins
                stopTimer();
                setTime(time);
                startTimer();
                return true;
            default:
                return false;
        }
    }

    public void setTime(long milliseconds){
        mtimeLeftInMillis = milliseconds;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(mtimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mtimeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
            }
        }.start();

        timerRunning = true;

    }

    public void stopTimer(){
        if(timerRunning){
            countDownTimer.cancel();
        }
        timerRunning = false;
    }

    public void updateTimer(){
        int mins = (int) (mtimeLeftInMillis / 1000) / 60;
        int secs = (int) (mtimeLeftInMillis / 1000) % 60;

        String timeLeftFormated = String.format(Locale.getDefault(),"%02d:%02d", mins, secs);
        btnTimer.setText(timeLeftFormated);
    }


}
