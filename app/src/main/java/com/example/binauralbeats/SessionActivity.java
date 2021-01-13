package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.binauralbeats.HomeActivity.EXTRA_BASE;
import static com.example.binauralbeats.HomeActivity.EXTRA_BEAT;
import static com.example.binauralbeats.HomeActivity.EXTRA_TIME;
import static com.example.binauralbeats.HomeActivity.EXTRA_TYPE;


public class SessionActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;

    //user data
    private int intSessionsCompleted;
    public int intTotalSessionTime;

    //buttons
    private Button btnPlay;
    public  TextView txtViewTimer;
    private Button btnAddMusic;
    private TextView mTxtViewBackground;

    //wave
    private BeatsEngine wave;
    float baseFrequency, beatFrequency;
    SeekBar seekbarWaveVolume;

    //timer
    private CountDownTimer countDownTimer;
    private long mtimeLeftInMillis;
    private boolean timerRunning;
    protected int timeLeftInMins;

    //music
    public static MediaPlayer player;
    SeekBar seekbarMusicVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        //sending an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //animated background start
        ConstraintLayout constraintLayout = findViewById(R.id.layout_session);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //passing values from recycler view library
        Intent intent = getIntent();
        String catType = intent.getStringExtra(EXTRA_TYPE);
        String catBaseFreq = intent.getStringExtra(EXTRA_BASE);
        String catBeatFreq = intent.getStringExtra(EXTRA_BEAT);
        String catTime = intent.getStringExtra(EXTRA_TIME);

        //change time to milliseconds for timer
        timeLeftInMins = Integer.parseInt(catTime);
        mtimeLeftInMillis = (timeLeftInMins * 60000);

        TextView textViewType = findViewById(R.id.textView_play_type_session);
        textViewType.setText(catType);

        btnPlay = findViewById(R.id.button_play_playBtn_session);
        btnAddMusic = findViewById(R.id.button_add_music_session);

        btnAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SessionActivity.this, btnAddMusic);
                popup.getMenuInflater().inflate(R.menu.popup_menu_music, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.item1:
                                stopMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item2:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.healing_water);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item3:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.in_the_light);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item4:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.nostalgia);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item5:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.quiet_time);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item6:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.sad_winds);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                            case R.id.item7:
                                stopMusic();
                                player = MediaPlayer.create(SessionActivity.this, R.raw.deep_meditation);
                                playMusic();
                                mTxtViewBackground.setText(item.getTitle());
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        baseFrequency = Float.parseFloat(catBaseFreq);
        beatFrequency = Float.parseFloat(catBeatFreq);
        //initialise new wave with frequencies
        wave = new Binaural(baseFrequency, beatFrequency);

        //wave volume control
        IntializeVolumeControls();

        //start the session
        txtViewTimer = findViewById(R.id.textView_timer_session);
        updateTimer();
        startSession();

        mTxtViewBackground = findViewById(R.id.textView_background_playing_session);
    }

    public void startSession(){
        btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_filled);
        wave.setVolume(seekbarWaveVolume.getProgress());
        wave.play();
        startTimer();
    }

    //play the session after the stop button is pressed
    public void playSession(View view){
        if(!wave.getIsPlaying()){
            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_filled);
            wave.setVolume(seekbarWaveVolume.getProgress());
            wave.play();
            startTimer();

        }else{
            btnPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
            wave.pause();
            stopTimer();
        }
    }

    /*
    //add background music
    public void addMusic(View view){
        Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
        startActivity(intent);
    }
*/

    private void IntializeVolumeControls() {
        seekbarWaveVolume = findViewById(R.id.seekBar_wave_volume_session);
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

        seekbarMusicVolume = findViewById(R.id.seekBar_music_volume_session);
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
           // Toast.makeText(this, "MediaPlayer released.", Toast.LENGTH_SHORT).show();
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

    /////////Timer////////////////

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


                if(mAuth.getCurrentUser() != null){
                    //get userid
                    String user_id = mAuth.getCurrentUser().getUid();
                    //save session completed for logged in user
                    final DatabaseReference current_user_sessions_completed_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("sessionsCompleted");
                    current_user_sessions_completed_db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String sessionsCompleted = String.valueOf(dataSnapshot.getValue());
                            intSessionsCompleted = Integer.parseInt(sessionsCompleted);
                            intSessionsCompleted++;
                            String sessionsCompletedString = String.valueOf(intSessionsCompleted);
                            current_user_sessions_completed_db.setValue(sessionsCompletedString);

                            //log event stats to Firebase Analytics
                            Bundle bundle = new Bundle();
                            bundle.putString("Sessions_completed", sessionsCompletedString);
                            mFirebaseAnalytics.logEvent("Sessions_Completed", bundle);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //save total session time updated for logged in user
                    final DatabaseReference current_user_total_session_time_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("totalSessionTimeMinutes");
                    current_user_total_session_time_db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String totalSessionTime = String.valueOf(dataSnapshot.getValue());
                            intTotalSessionTime = Integer.parseInt(totalSessionTime);
                            intTotalSessionTime += timeLeftInMins;
                            String totalSessionTimeString = String.valueOf(intTotalSessionTime);
                            current_user_total_session_time_db.setValue(totalSessionTimeString);

                            //log event stats to Firebase Analytics
                            Bundle bundle = new Bundle();
                            bundle.putString("Total_Session_Time_Minutes", totalSessionTimeString);
                            mFirebaseAnalytics.logEvent("Total_Session_Time_Minutes", bundle);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                stopMusic();

                if(wave.getIsPlaying()){
                    if (wave != null) {
                        wave.release();
                    }
                }
                finish();

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
        txtViewTimer.setText(timeLeftFormated);
    }

}
