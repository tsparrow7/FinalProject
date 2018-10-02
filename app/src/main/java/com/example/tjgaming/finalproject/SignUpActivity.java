package com.example.tjgaming.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mFirebaseAuth;

    EditText mNameText;
    EditText mAddressText;
    EditText mEmailText;
    EditText mMobileText;
    EditText mPasswordText;
    EditText mReEnterPasswordText;
    Button mSignUpButton;
    TextView mLoginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeViews();
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
    }

    private void initializeViews() {
        mNameText = findViewById(R.id.input_name);
        mAddressText = findViewById(R.id.input_address);
        mEmailText = findViewById(R.id.input_email);
        mMobileText = findViewById(R.id.input_mobile);
        mPasswordText = findViewById(R.id.input_password);
        mReEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        mSignUpButton = findViewById(R.id.btn_signup);
        mLoginLink = findViewById(R.id.link_login);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

//        String name = mNameText.getText().toString();
//        String address = mAddressText.getText().toString();
        String email = mEmailText.getText().toString();
//        String mobile = mMobileText.getText().toString();
        String password = mPasswordText.getText().toString();
//        String reEnterPassword = mReEnterPasswordText.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Log.d(TAG,"createUserSuccess");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Log.w(TAG,"createUserFailure");
                            Toast.makeText(getApplicationContext(),"Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void onSignupSuccess() {
        mSignUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mSignUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String address = mAddressText.getText().toString();
        String email = mEmailText.getText().toString();
        String mobile = mMobileText.getText().toString();
        String password = mPasswordText.getText().toString();
        String reEnterPassword = mReEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            mNameText.setError("at least 2 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (address.isEmpty()) {
            mAddressText.setError("Enter Valid Address");
            valid = false;
        } else {
            mAddressText.setError(null);
        }


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mMobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mMobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            mReEnterPasswordText.setError("Passwords Do not match");
            valid = false;
        } else {
            mReEnterPasswordText.setError(null);
        }

        return valid;
    }
}
