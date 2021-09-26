package com.broscr.iptvplayer;

import android.app.Application;

import io.realm.Realm;
import timber.log.Timber;

public class IPTVPlayer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
