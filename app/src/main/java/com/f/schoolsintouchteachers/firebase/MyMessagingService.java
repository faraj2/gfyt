package com.f.schoolsintouchteachers.firebase;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.f.schoolsintouchteachers.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification()!=null){
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setSmallIcon(R.drawable.fingerclick);
             builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat manager=NotificationManagerCompat.from(this);
            manager.notify(101,builder.build());
        }
    }

}
