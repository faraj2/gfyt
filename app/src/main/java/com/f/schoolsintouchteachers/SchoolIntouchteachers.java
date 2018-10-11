package com.f.schoolsintouchteachers;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.f.schoolsintouchteachers.models.realmObjects.MyMigration;
import com.github.johnkil.print.PrintConfig;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by admin on 3/15/2018.
 */

public class SchoolIntouchteachers extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrintConfig.initDefault(getAssets(), "fonts/material-icon-font.ttf");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().schemaVersion(4).migration(new MyMigration()).name("home.realm").build();
        Realm.setDefaultConfiguration(config);

        JodaTimeAndroid.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
