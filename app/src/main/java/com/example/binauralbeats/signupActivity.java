package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class signupActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout mEditTextUsername, mEditTextEmail, mEditTextPassword;
    ProgressBar mProgressBarSignup;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEditTextUsername = findViewById(R.id.text_input_username_signup);
        mEditTextEmail = findViewById(R.id.text_input_email_signup);
        mEditTextPassword = findViewById(R.id.text_input_pass_signup);
        mProgressBarSignup = findViewById(R.id.progressBarSignup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //sending an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        findViewById(R.id.button_signup).setOnClickListener(this);
        findViewById(R.id.textView_goLogin).setOnClickListener(this);

    }

    private void registerUser(){
        final String username = mEditTextUsername.getEditText().getText().toString().trim();

        //username
        if(username.isEmpty()){
            mEditTextUsername.setError(getString(R.string.Username_empty));
            mEditTextUsername.requestFocus();
            return;
        }

        if(username.length() > 20){
            mEditTextUsername.setError(getString(R.string.Username_error));
            mEditTextUsername.requestFocus();
            return;
        }

        Query usernameQuery = mDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    Toast.makeText(signupActivity.this, getString(R.string.Toast_username), Toast.LENGTH_SHORT).show();
                }else{

                    final String email = mEditTextEmail.getEditText().getText().toString().trim();
                    final String password = mEditTextPassword.getEditText().getText().toString().trim();

                    //email
                    if(email.isEmpty()){
                        mEditTextEmail.setError(getString(R.string.Email_empty));
                        mEditTextEmail.requestFocus();
                        return;
                    }

                    if(email.length() > 30){
                        mEditTextEmail.setError(getString(R.string.Email_error));
                        mEditTextEmail.requestFocus();
                        return;
                    }

                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        mEditTextEmail.setError(getString(R.string.Email_error_invalid));
                        mEditTextEmail.requestFocus();
                        return;
                    }

                    //password
                    if(password.isEmpty()){
                        mEditTextPassword.setError(getString(R.string.Password_empty));
                        mEditTextPassword.requestFocus();
                        return;
                    }

                    if(password.length() < 6){
                        mEditTextPassword.setError(getString(R.string.Password_error));
                        mEditTextPassword.requestFocus();
                        return;
                    }

                    mProgressBarSignup.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBarSignup.setVisibility(View.GONE);

                            if(task.isSuccessful()){
                                finish();
                                Toast.makeText(getApplicationContext(), "User registered successfully.", Toast.LENGTH_SHORT).show();

                                //send login event
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.METHOD, email);
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                //save username
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

                                Map newPost = new HashMap();
                                newPost.put("username", username);
                                newPost.put("email", email);
                                newPost.put("sessionsCompleted", "0");
                                newPost.put("totalSessionTimeMinutes", "0");

                                current_user_db.setValue(newPost);

                                Intent intent = new Intent(signupActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else{

                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), getString(R.string.Toast_email), Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_signup:
                registerUser();
                break;
            case R.id.textView_goLogin:
                finish();
                startActivity(new Intent(this, loginActivity.class));
                break;
        }
    }
}
