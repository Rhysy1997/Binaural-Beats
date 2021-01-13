package com.example.binauralbeats;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.graphics.Color;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    TextInputLayout binauralTitle;
    TextInputLayout baseFrequencyInput;
    TextInputLayout beatFrequencyInput;
    public TextView waveType;

    SeekBar beatSeekBar;
    SeekBar baseSeekBar;

    DatabaseReference databaseTracks;

    private boolean isTitleValid = false, isBaseValid = false, isBeatValid = false;
    private BeatsEngine wave;

    TextWatcher textWatcherTitle = new TextWatcher() {

        public void afterTextChanged(Editable s)
        {
            isTitleValid = false;
            if( binauralTitle.getEditText().getText().toString().trim().length() == 0 )
                binauralTitle.setError(getString(R.string.Title_error_empty));
            else {
                if (binauralTitle.getEditText().getText().toString().trim().length() > 15 )
                    binauralTitle.setError(getString(R.string.Title_error_chars));
                else {
                    isTitleValid = true;
                    binauralTitle.setError(null);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {


        }

        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {


        }

    };

    TextWatcher textWatcherBase = new TextWatcher() {

        public void afterTextChanged(Editable s)
        {
            isBaseValid = false;
            if( baseFrequencyInput.getEditText().getText().toString().trim().length() == 0 )
                baseFrequencyInput.setError( getString(R.string.Base_Error_empty));
            else {
                try {
                    float baseFrequency = Float.parseFloat(baseFrequencyInput.getEditText().getText().toString().trim());
                    if (baseFrequency < 21 || baseFrequency > 1200)
                        baseFrequencyInput.setError( getString(R.string.Base_Error_values) );
                    else {
                        isBaseValid = true;
                        baseFrequencyInput.setError(null);
                    }
                }catch (NumberFormatException ex) {
                    baseFrequencyInput.setError( ex.toString() );
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

    };

    TextWatcher textWatcherBeat = new TextWatcher() {

        public void afterTextChanged(Editable s)
        {
            isBeatValid = false;
            if( beatFrequencyInput.getEditText().getText().toString().length() == 0 )
                beatFrequencyInput.setError( getString(R.string.Beat_Error_empty));
            else {
                float beatFrequency;
                try {
                    beatFrequency= Float.parseFloat(beatFrequencyInput.getEditText().getText().toString().trim());

                    if (beatFrequency < 1 || beatFrequency > 40)
                        beatFrequencyInput.setError(getString(R.string.Beat_Error_values));
                    else {
                        isBeatValid = true;
                        beatFrequencyInput.setError(null);
                    }


                    waveType.setVisibility(View.VISIBLE);
                    //change wave type textview
                    if(beatFrequency >= 1 && beatFrequency <= 3){
                        waveType.setText("Delta");
                        waveType.setTextColor(getColor(R.color.colorDelta));
                    }else if(beatFrequency >= 4 && beatFrequency <= 8){
                        waveType.setText("Theta");
                        waveType.setTextColor(getColor(R.color.colorTheta));
                    }else if(beatFrequency >= 9 && beatFrequency <= 13) {
                        waveType.setText("Alpha");
                        waveType.setTextColor(getColor(R.color.colorAlpha));
                    }else if(beatFrequency >= 14 && beatFrequency <= 30) {
                        waveType.setText("Beta");
                        waveType.setTextColor(getColor(R.color.colorBeta));
                    }else if(beatFrequency >= 31 && beatFrequency <= 40) {
                        waveType.setText("Gamma");
                        waveType.setTextColor(getColor(R.color.colorGamma));
                    }
                } catch (NumberFormatException ex)
                {
                    beatFrequencyInput.setError( ex.toString() );
                }
            }



        }

        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sending an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initializeView();

        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_create);

        //item selected lisener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();

                        return true;
                    case R.id.nav_library:
                        Intent intent1 = new Intent(getApplicationContext(), LibraryActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        finish();

                        return true;
                    case R.id.nav_create:
                        return true;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();

                        return true;
                }
                return false;
            }
        });
    }

    public void beatSeekBar(){
        beatSeekBar = findViewById(R.id.seekBarBeat);
        beatSeekBar.setMax(40);
        beatSeekBar.setMin(1);
        beatSeekBar.setProgress(0);
        beatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                beatFrequencyInput.getEditText().setText("" + progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void baseSeekBar(){
        baseSeekBar = (SeekBar)findViewById(R.id.seekBarBase);
        baseSeekBar.setMax(1200);
        baseSeekBar.setMin(21);
        baseSeekBar.setProgress(0);
        baseSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                baseFrequencyInput.getEditText().setText(""+ progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initializeView() {
        binauralTitle = findViewById(R.id.text_input_title);
        baseFrequencyInput = findViewById(R.id.text_input_base);
        beatFrequencyInput = findViewById(R.id.text_input_beat);
        waveType = (TextView) findViewById(R.id.textView_waveType);
        waveType.setVisibility(View.INVISIBLE);
        waveType.setText("hi");

        beatSeekBar();
        baseSeekBar();
        baseFrequencyInput.getEditText().addTextChangedListener(textWatcherBase);
        beatFrequencyInput.getEditText().addTextChangedListener(textWatcherBeat);
        binauralTitle.getEditText().addTextChangedListener(textWatcherTitle);

        //saving to database
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks");
    }

    /*
    private boolean validateTitle(){
        String titleInput = binauralTitle.getEditText().getText().toString().trim();
        if(titleInput.isEmpty()){
            binauralTitle.setError("Value cannot be empty");
        }
    }

     */

    //create beats methods
    public void clickTest(View v){
        attemptTest();
    }

    private void attemptTest(){
        if (isBeatValid ==true && isBaseValid ==true) {
            refresh();
            if(!wave.getIsPlaying()){
                wave.start();
            }
        }
    }

    public void clickStop(View v){
        attemptStop();
    }

    private void attemptStop(){
        if(wave != null){
            wave.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (wave != null) {
            wave.release();
        }
    }

    //Refresh the tones
    private void refresh() {
        float baseFrequency, beatFrequency;

        baseFrequency = Float.parseFloat(baseFrequencyInput.getEditText().getText().toString());
        beatFrequency = Float.parseFloat(beatFrequencyInput.getEditText().getText().toString());

        if (wave != null) {
            wave.release();
        }

        wave = new Binaural(baseFrequency, beatFrequency);
    }

    //database save methods
    public void clickSave(View v){
        saveTones();
    }

    private void saveTones(){
        if (isBeatValid && isBaseValid && isTitleValid) {

            String title = binauralTitle.getEditText().getText().toString().trim();
            String baseFreq = baseFrequencyInput.getEditText().getText().toString();
            String beatFreq = beatFrequencyInput.getEditText().getText().toString();
            String mWaveType = waveType.getText().toString().trim();

            //database
            String id = databaseTracks.push().getKey();
            Track track = new Track(id, title, baseFreq, beatFreq, mWaveType);
            databaseTracks.child(id).setValue(track);

            Toast.makeText(this, getString(R.string.Toast_saved), Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, getString(R.string.Toast_error), Toast.LENGTH_LONG).show();
        }

    }

}
