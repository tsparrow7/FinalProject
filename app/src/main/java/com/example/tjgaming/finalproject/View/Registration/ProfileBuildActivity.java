package com.example.tjgaming.finalproject.View.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tjgaming.finalproject.R;

public class ProfileBuildActivity extends AppCompatActivity {

    private AppCompatButton mMaleButton;
    private AppCompatButton mFemaleButton;
    private AppCompatButton mContinueButton;
    private AppCompatCheckBox mNotificationCheckBox;
    private AppCompatSpinner mDaySpinner;
    private AppCompatSpinner mMonthSpinner;
    private AppCompatSpinner mYearSpinner;
    private EditText mUserNameInput;

    private String mUid;
    private String mEmail;
    private String mGender;
    private String mMonth;
    private String mDay;
    private String mYear;
    private String mUserName;
    private boolean mNotifications;
    private int mMaleBgColor;
    private int mFemaleBgColor;
    private int unselectedColor;
    private int selectedColor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_build);

        mUid = getIntent().getStringExtra("uid");
        mEmail = getIntent().getStringExtra("email");
        unselectedColor = getResources().getColor(R.color.accent);
        selectedColor = getResources().getColor(R.color.primary_darker);

        initializeViews();
        setListeners();
        setSpinners();
    }

    private void initializeViews() {
        mFemaleButton = findViewById(R.id.profile_female_btn);
        mMaleButton = findViewById(R.id.profile_male_btn);
        mContinueButton = findViewById(R.id.btn_profile_continue);
        mNotificationCheckBox = findViewById(R.id.profile_notify_checkbox);
        mUserNameInput = findViewById(R.id.profile_username);
        mDaySpinner = findViewById(R.id.profile_spinner_day);
        mMonthSpinner = findViewById(R.id.profile_spinner_month);
        mYearSpinner = findViewById(R.id.profile_spinner_year);
    }

    private void setListeners() {

        mFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mFemaleBgColor = ( (ColorDrawable)mFemaleButton.getBackground() ).getColor();
                mMaleBgColor = ( (ColorDrawable)mMaleButton.getBackground() ).getColor();

                if (mMaleBgColor == unselectedColor) {

                    if (mFemaleBgColor == getResources().getColor(R.color.accent)) {
                        mFemaleButton.setBackgroundColor(selectedColor);
                    } else {
                        mFemaleButton.setBackgroundColor(unselectedColor);
                    }
                } else {
                    mMaleButton.setBackgroundColor(unselectedColor);
                    mFemaleButton.setBackgroundColor(selectedColor);
                }
            }
        });

        mMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleBgColor = ( (ColorDrawable)mFemaleButton.getBackground() ).getColor();
                mMaleBgColor = ( (ColorDrawable)mMaleButton.getBackground() ).getColor();

                if(mFemaleBgColor == unselectedColor) {

                    if (mMaleBgColor == unselectedColor) {
                        mMaleButton.setBackgroundColor(selectedColor);
                    } else {
                        mMaleButton.setBackgroundColor(unselectedColor);
                    }
                } else {
                    mFemaleButton.setBackgroundColor(unselectedColor);
                    mMaleButton.setBackgroundColor(selectedColor);
                }

                mFemaleBgColor = ( (ColorDrawable)mFemaleButton.getBackground() ).getColor();
                mMaleBgColor = ( (ColorDrawable)mMaleButton.getBackground() ).getColor();

                if (mFemaleBgColor == selectedColor)
                    mGender = "Female";
                else if (mMaleBgColor == selectedColor)
                    mGender = "Male";
                else if (mMaleBgColor == unselectedColor && mFemaleBgColor == unselectedColor)
                    mGender = "N/A";
            }
        });

        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonth = mMonthSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDay = mDaySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear = mYearSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startProgress();
                mContinueButton.setEnabled(false);
                mUserName = mUserNameInput.getText().toString();

                if (validateFields()) {

                    stopProgress();

                    Intent mediaSelectIntent = new Intent(getApplicationContext(), MediaSelectionActivity.class);
                    mediaSelectIntent.putExtra("uid", mUid);
                    mediaSelectIntent.putExtra("email", mEmail);
                    mediaSelectIntent.putExtra("gender", mGender);
                    mediaSelectIntent.putExtra("dateOfBirth",mMonth + mDay + mYear);
                    mediaSelectIntent.putExtra("username", mUserName);
                    mediaSelectIntent.putExtra("notifications", mNotifications);
                    startActivity(mediaSelectIntent);
                    finish();
                } else {
                    Toast.makeText(ProfileBuildActivity.this, "Please Fill Out All Fields.", Toast.LENGTH_SHORT).show();
                    mContinueButton.setEnabled(true);
                    stopProgress();
                }
            }
        });
    }

    private void setSpinners() {

        mMonthSpinner = findViewById(R.id.profile_spinner_month);
        ArrayAdapter<CharSequence> mthAdapter = ArrayAdapter.createFromResource(this, R.array.Month, android.R.layout.simple_spinner_item);
        mthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthSpinner.setAdapter(mthAdapter);

        mDaySpinner = findViewById(R.id.profile_spinner_day);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.Day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDaySpinner.setAdapter(dayAdapter);

        mYearSpinner = findViewById(R.id.profile_spinner_year);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.Year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearSpinner.setAdapter(yearAdapter);
    }

    private boolean validateFields() {

        return mGender != null && !mMonth.equals("Month") && !mDay.equals("Day") && !mYear.equals("Year") && !mUserName.equals("");
    }

    private void startProgress() {
        progressDialog = new ProgressDialog(ProfileBuildActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    private void stopProgress() {
        progressDialog.dismiss();
    }

}