package com.athome;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Lady on 18/12/2015.
 */
public class AtHome extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
