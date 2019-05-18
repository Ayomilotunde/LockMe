package com.sanxynet.lockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LockScreenActivity extends AppCompatActivity {

    String time, currentDateandTime, tomorrow, currentTime;
    private static final String PACKAGE_NAME = "com.sanxynet.lockme";
    private static final String TIME = "time";
    private static final String TOMORROW = "tomorrow";
    private static final String CURRENT_DATE = "currentDateandTime";
    SharedPreferences sharedPrefs;
    Calendar calander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_lock);
        UnlockBar unlock = findViewById(R.id.unlock);

        // Attach listener
        unlock.setOnUnlockListenerRight(this::goToPinPatternLock);

        unlock.setOnUnlockListenerLeft(this::goToPinPatternLock);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        currentDateandTime = sdf.format(new Date());

        calander = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        currentTime = new SimpleDateFormat("HH:mm", Locale.US).format(new Date());

//        time = simpleDateFormat.format(calander.getTime());

        calander.add(Calendar.DATE, 1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        tomorrow = sdf1.format(calander.getTime());

        sharedPrefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TIME, currentTime);
        editor.putString(CURRENT_DATE, currentDateandTime);
        editor.putString(TOMORROW, tomorrow);
        editor.apply();


        downloadImage();
    }

    @Override
    public void onAttachedToWindow() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onAttachedToWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LockApplication) getApplication()).lockScreenShow = true;
        downloadImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LockApplication) getApplication()).lockScreenShow = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void goToPinPatternLock(){
        Intent pinPattern = new Intent(LockScreenActivity.this, CheckPinPatternActivity.class);
        startActivity(pinPattern);
    }

    public void downloadImage(){
        ParseObject object = new ParseObject("imageupload"); // class name

        ParseFile postImage = object.getParseFile("ImageFile"); // column name

        String currentDate = sharedPrefs.getString(CURRENT_DATE, null);
        String tomorrowDate = sharedPrefs.getString(TOMORROW, null);
        String timeNow = sharedPrefs.getString(TIME, null);


        ParseQuery<ParseObject> getimage = new ParseQuery<>("imageupload"); // class
        getimage.whereGreaterThanOrEqualTo("date", currentDate);
        getimage.whereLessThan("date", tomorrowDate);
//        getimage.whereLessThan("endTime", timeNow);
//        getimage.whereGreaterThanOrEqualTo("startTime", timeNow);
        getimage.findInBackground((objects, e) -> {

            if (e == null) {
                // success
//                Collections.shuffle(objects);
                for (ParseObject parseObject : objects) {

                    ParseFile fileObject = (ParseFile) parseObject.get("ImageFile");  // column name

                    assert fileObject != null;
                    Log.d("test", "get your image ... " + fileObject.getUrl());

//                    Toast.makeText(this, timeNow, Toast.LENGTH_LONG).show();
                    ImageView imageView = findViewById(R.id.image_background);
                    Picasso.get()
                            .load(fileObject.getUrl())
                            .placeholder(R.drawable.iklan)
                            .error(R.drawable.iklan)
                            .into(imageView);
                }
            } else {
                // fail
                Log.d("test", "error Message... " + e.getMessage());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        downloadImage();
    }
}
