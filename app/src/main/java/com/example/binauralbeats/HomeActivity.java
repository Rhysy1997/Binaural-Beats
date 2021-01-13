package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener{

    DatabaseReference databaseCategory;

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_BASE = "baseFreq";
    public static final String EXTRA_BEAT = "beatFreq";
    public static final String EXTRA_TIME = "time";

    //new code
    private RecyclerView mRecyclerViewHome;
    private CategoryAdapter mCatAdapter;
    private ArrayList<Category> mCatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseCategory = FirebaseDatabase.getInstance().getReference("category");

        //new code
        mRecyclerViewHome = findViewById(R.id.recycler_view_home);
        mRecyclerViewHome.setHasFixedSize(true);
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(this));
        mCatList = new ArrayList<>();


        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //item selected lisener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_library:
                        Intent intent = new Intent(getApplicationContext(), LibraryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();

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

        databaseCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mCatList.clear();

                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
                    Category category = trackSnapshot.getValue(Category.class);

                    mCatList.add(category);
                }

                mCatAdapter = new CategoryAdapter((HomeActivity.this), mCatList);

                try{

                    mRecyclerViewHome.setAdapter(mCatAdapter);
                    mCatAdapter.setOnItemClickListener(HomeActivity.this);


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

        Intent sessionActivityIntent = new Intent(this, SessionActivity.class);

        Category clickedItem = mCatList.get(position);

        sessionActivityIntent.putExtra(EXTRA_TYPE, clickedItem.getCatType());
        sessionActivityIntent.putExtra(EXTRA_BASE, clickedItem.getCatBaseFreq());
        sessionActivityIntent.putExtra(EXTRA_BEAT, clickedItem.getCatBeatFreq());
        sessionActivityIntent.putExtra(EXTRA_TIME, clickedItem.getCatTime());

        startActivity(sessionActivityIntent);
    }
}
