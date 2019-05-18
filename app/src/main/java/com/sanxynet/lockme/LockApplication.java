package com.sanxynet.lockme;

import android.app.Application;
import com.parse.Parse;


public class LockApplication extends Application {

   public boolean lockScreenShow = false;
    public int notificationId = 1989;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APP_ID)
                .clientKey(BuildConfig.CLIENT_KEY)
                .server(getString(R.string.back4app_server_url))
                .build()
        );

    }

}
