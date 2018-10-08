package com.f.schoolsintouchteachers.models;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by admin on 3/15/2018.
 */

public class Models {
    DataSnapshot snapshot;
    private static final Models ourInstance = new Models();

    public static Models getInstance() {



        return ourInstance;
    }

    public  DataSnapshot getSnapshot() {
        return snapshot;
    }

    public  void setSnapshot(DataSnapshot snapshot) {
       this.snapshot = snapshot;
    }

    private Models() {
    }
}
