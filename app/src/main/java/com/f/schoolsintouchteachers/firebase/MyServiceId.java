package com.f.schoolsintouchteachers.firebase;

import android.util.Log;

import com.f.schoolsintouchteachers.models.DBHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyServiceId extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("MYRESRESHEDTKEN",token);

        getSharedPreferences("Token",MODE_PRIVATE).edit().putString("mytoken",token).apply();



    }
}
