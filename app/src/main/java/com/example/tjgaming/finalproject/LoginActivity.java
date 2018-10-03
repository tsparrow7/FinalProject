package com.example.tjgaming.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by TJ on 10/1/2018.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog progressDialog;
    private boolean firstTimeUser = false;

    EditText mEmailText;
    EditText mPasswordText;
    Button mLoginButton;
    TextView mSignUpLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        mSignUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
    }

    private void initializeViews() {
        mEmailText = findViewById(R.id.input_email);
        mPasswordText = findViewById(R.id.input_password);
        mLoginButton = findViewById(R.id.btn_login);
        mSignUpLink = findViewById(R.id.link_signup);

        firstTimeUser = getIntent().getBooleanExtra("firstTimeUser", false);

        if (firstTimeUser) {
            String email = getIntent().getStringExtra("email");
            mEmailText.setText(email);
            mEmailText.clearFocus();
            mPasswordText.requestFocus();
        }

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        startProgress();

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        stopProgress();

                        if (task.isSuccessful()){
                            Log.d(TAG,"signInSuccess");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            if (firstTimeUser){
                                //TODO: launch profile building activities
                                Toast.makeText(getApplicationContext(),"First time logging in.",Toast.LENGTH_SHORT).show();
                            } else {
                                //TODO: launch main page
                                Toast.makeText(getApplicationContext(),"Been logged in before.",Toast.LENGTH_SHORT).show();
                            }
                            //onLoginSuccess();

                        } else {
                            Log.w(TAG,"signInFailure");
                            Toast.makeText(getApplicationContext(),"Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startProgress() {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    private void stopProgress() {
        progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        mLoginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            mPasswordText.setError("between 6 and 10 characters long");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        return valid;
    }
}
