package com.example.binauralbeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout mEditTextEmail, mEditTextPassword;
    ProgressBar mProgressBarLogin;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = findViewById(R.id.text_input_email_login);
        mEditTextPassword = findViewById(R.id.text_input_pass_login);
        mProgressBarLogin = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

        //sending an event
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        findViewById(R.id.textView_goSignup).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);

    }

    private void userLogin(){
        final String email = mEditTextEmail.getEditText().getText().toString().trim();
        String password = mEditTextPassword.getEditText().getText().toString().trim();

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

        mProgressBarLogin.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBarLogin.setVisibility(View.GONE);

                if(task.isSuccessful()){

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.METHOD, email);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                    finish();
                    Intent intent = new Intent(loginActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_login:
                userLogin();
                break;
            case R.id.textView_goSignup:
                finish();
                startActivity(new Intent(this, signupActivity.class));
                break;
        }
    }
}
