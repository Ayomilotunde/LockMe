package com.sanxynet.lockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.amirarcane.lockscreen.activity.EnterPinActivity;


public class CheckPinPatternActivity extends AppCompatActivity {
    private static final String PACKAGE_NAME = "com.sanxynet.lockme";
    private static final String PIN_FINGERPRINT_SAVED = "pinEnabled";
    private static final String PATTERN_SAVED = "patternEnabled";
    private static final int REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pin_pattern);

        checkPinLock();
        checkPatternLock();

    }

    private void checkPinLock(){
        SharedPreferences sharedPrefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
        if (sharedPrefs.contains(PIN_FINGERPRINT_SAVED)) {
            if (sharedPrefs.getBoolean(PIN_FINGERPRINT_SAVED, true)) {
                /* Enable lock with pin or fingerprint */
//                Intent intent = new Intent(CheckPinPatternActivity.this, EnterPin2Activity.class);
//                startActivity(intent);
                /* Enable lock with pin or fingerprint */
                Intent intent = new Intent(CheckPinPatternActivity.this, EnterPinActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (!sharedPrefs.contains(PIN_FINGERPRINT_SAVED)) {
            exit();
        }
    }

    private void checkPatternLock(){
        SharedPreferences sharedPrefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
        if (sharedPrefs.contains(PATTERN_SAVED)){
            Intent showEnterPattern = new Intent(CheckPinPatternActivity.this, EnterPatternActivity.class);
            startActivity(showEnterPattern);
        }else if (!sharedPrefs.contains(PATTERN_SAVED)){
            exit();
        }
    }


    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent goBack = new Intent(getApplicationContext(), LockScreenActivity.class);
        goBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(goBack);
    }

    private void goBack(){
        Intent goBack = new Intent(getApplicationContext(), LockScreenActivity.class);
        goBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(goBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPinLock();
        checkPatternLock();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == EnterPinActivity.RESULT_OK) {
                    Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);

                    if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
                        finish();
                    }

                }else {
//                    exit();
                    goBack();
                }

        }
    }
}
