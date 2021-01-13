package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener{

    DatabaseReference databaseTracks;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_BASE = "baseFreq";
    public static final String EXTRA_BEAT = "beatFreq";

    //new code
    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<Track> mExampleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks");

        //new code
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mExampleList = new ArrayList<>();


        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_library);
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
                        return true;
                    case R.id.nav_create:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        finish();

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

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mExampleList.clear();

                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
                    Track track = trackSnapshot.getValue(Track.class);

                    mExampleList.add(track);
                }

                 mExampleAdapter = new ExampleAdapter((LibraryActivity.this), mExampleList);

                try{

                        mRecyclerView.setAdapter(mExampleAdapter);
                        mExampleAdapter.setOnItemClickListener(LibraryActivity.this);


                }catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        //Toast.makeText(this,  "Position " + position + " touched.", Toast.LENGTH_LONG).show();

        Intent playActivityIntent = new Intent(this, PlayActivity.class);

        Track clickedItem = mExampleList.get(position);

        playActivityIntent.putExtra(EXTRA_TITLE, clickedItem.getTrackTitle());
        playActivityIntent.putExtra(EXTRA_BASE, clickedItem.getBaseFreq());
        playActivityIntent.putExtra(EXTRA_BEAT, clickedItem.getBeatFreq());

        startActivity(playActivityIntent);
    }

}
