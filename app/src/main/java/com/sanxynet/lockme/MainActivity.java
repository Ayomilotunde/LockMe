package com.sanxynet.lockme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.google.android.material.snackbar.Snackbar;
import com.sanxynet.lockme.utils.LockScreen;


public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    private static final String PACKAGE_NAME = "com.sanxynet.lockme";
    private static final String PIN_FINGERPRINT_SAVED = "pinEnabled";
    private static final String PATTERN_SAVED = "patternEnabled";
    private Snackbar mSnackbar;
    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton = findViewById(R.id.toggleButton);
        LockScreen.getInstance().init(this,true);
        if(LockScreen.getInstance().isActive()){
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);

        }


        toggleButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked){

                LockScreen.getInstance().active();
            }else{
                LockScreen.getInstance().deactivate();
            }
        });

        findViewById(R.id.buttonPin).setOnClickListener(v -> {
            // set pin instead of checking it
//            Intent intent = new Intent(MainActivity.this, EnterPinActivity.class);
            Intent intent = EnterPinActivity.getIntent(MainActivity.this, true);
//            startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE);
        });

        findViewById(R.id.buttonPattern).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PatternActivity.class)));

        findViewById(R.id.buttonClear).setOnClickListener(v ->
                clearPinPattern()
        );

        onlyOneLock();

    }

    @Override
    protected void onResume() {
        super.onResume();
        onlyOneLock();
    }

    private void showSnackbar(String text) {
        mSnackbar = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
        mSnackbar.setAction(R.string.action_close, view -> mSnackbar.dismiss());
        mSnackbar.show();
    }

    private void clearPinPattern(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_pin_pattern);
        builder.setMessage(R.string.pin_pattern_will_be_empty);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            /* clear pin */
            SharedPreferences.Editor editor = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE).edit();
            editor.remove(PIN_FINGERPRINT_SAVED);
            editor.remove(PATTERN_SAVED);
            editor.apply();
//            LockManager lockManager = LockManager.getInstance();
//            lockManager.getAppLock().setPasscode(null);

            showSnackbar(getString(R.string.pin_cleared));
            refresh();
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onlyOneLock(){
        SharedPreferences sharedPrefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
        if (!sharedPrefs.contains(PATTERN_SAVED) && !sharedPrefs.contains(PIN_FINGERPRINT_SAVED)){
            findViewById(R.id.buttonPin).setEnabled(true);
            findViewById(R.id.buttonPattern).setEnabled(true);
        }
        if (sharedPrefs.contains(PATTERN_SAVED) ){
            findViewById(R.id.buttonPin).setEnabled(false);
        }
        if (sharedPrefs.contains(PIN_FINGERPRINT_SAVED) ){
            findViewById(R.id.buttonPattern).setEnabled(false);
        }
    }


    public void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == EnterPinActivity.RESULT_OK) {
                    SharedPreferences.Editor editor = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PIN_FINGERPRINT_SAVED, true);
                    editor.apply();
                    showSnackbar(getString(R.string.pin_enabled));
                }
                break;
        }
    }
}
