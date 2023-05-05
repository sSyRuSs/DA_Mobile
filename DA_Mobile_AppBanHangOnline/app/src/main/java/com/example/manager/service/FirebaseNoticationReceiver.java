package com.example.manager.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;

import com.example.appbanhangonline.R;
import com.example.manager.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNoticationReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if ( message.getNotification() != null){
            showNotication(message.getNotification().getTitle(),message.getNotification().getBody());
        }
    }

    private void showNotication(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelId = "noti";
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT| PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),channelId)
                .setSmallIcon(com.google.android.gms.base.R.drawable.googleg_standard_color_18)
                        .setAutoCancel(true)
                                .setVibrate(new long[]{1000,1000,1000,1000})
                                        .setOnlyAlertOnce(true)
                                                .setContentIntent(pendingIntent);
        builder = builder.setContent(customView(title,body));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelId,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }

    private RemoteViews customView(String title, String body){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notication);
        remoteViews.setTextViewText(R.id.text_notication,title);
        remoteViews.setTextViewText(R.id.body_notication,body);
        remoteViews.setImageViewResource(R.id.image_notication, com.google.android.gms.base.R.drawable.googleg_standard_color_18);
        return remoteViews;

    }
}
