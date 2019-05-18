package com.sanxynet.lockme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;

public class SetPinActivity extends PinCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    private static final String PACKAGE_NAME = "com.sanxynet.lockme";
    private static final String PIN_FINGERPRINT_SAVED = "pinEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_set);

        this.findViewById(R.id.button_enable_pin).setOnClickListener(this);
        this.findViewById(R.id.button_change_pin).setOnClickListener(this);
        this.findViewById(R.id.button_unlock_pin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SetPinActivity.this, CustomPinActivity.class);
        switch (v.getId()) {
            case R.id.button_enable_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
                break;
            case R.id.button_change_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                break;
            case R.id.button_unlock_pin:
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                startActivity(intent);
                /* clear pin */
                SharedPreferences.Editor editor = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE).edit();
                editor.remove(PIN_FINGERPRINT_SAVED);
                editor.apply();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PIN_FINGERPRINT_SAVED, true);
                editor.apply();
                break;
        }
    }

}
