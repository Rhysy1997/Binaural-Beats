package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    TextView mEmail, mUsername, mSessionsCompleted, mTotalSessionTime;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEmail = findViewById(R.id.textView_profileEmail);
        mUsername = findViewById(R.id.textView_profileUsername);

        mSessionsCompleted = findViewById(R.id.textView_display_sessions_completed);
      //  mSessionsCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hourglass_sessions_24dp, 0, 0, 0);

        mTotalSessionTime = findViewById(R.id.textView_display_total_session_time);
     //   mTotalSessionTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_total_session_time_24dp, 0, 0, 0);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        if(mAuth.getCurrentUser() != null){
            //get userid
            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

            current_user_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username = String.valueOf(dataSnapshot.child("username").getValue());
                    mUsername.setText(username);
                    String sessionsCompleted = String.valueOf(dataSnapshot.child("sessionsCompleted").getValue());
                    mSessionsCompleted.setText(sessionsCompleted);
                    String totalSessionTime = String.valueOf(dataSnapshot.child("totalSessionTimeMinutes").getValue());
                    mTotalSessionTime.setText(totalSessionTime + " min");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }else{
            mEmail.setText("Logged in as " + mAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_logout:
                FirebaseAuth.getInstance().signOut(); //logout
                finish();
                startActivity(new Intent(this, HomeActivity.class));
                break;

        }
    }
}
