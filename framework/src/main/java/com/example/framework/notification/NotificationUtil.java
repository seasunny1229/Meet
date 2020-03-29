package com.example.framework.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.framework.R;

public class NotificationUtil {

    public static void createChannels(Context context, String[] channelIds, String[] channelNames){

        // notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager == null){
            return;
        }

        // register
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            for(int i=0;i<channelIds.length;i++){
                NotificationChannel notificationChannel = new NotificationChannel(channelIds[i], channelNames[i], NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }


    public static void pushMessageNotification(Context context,
                                               String channelId,
                                               String objectId,
                                               String title,
                                               String text,
                                               Bitmap bitmap,
                                               PendingIntent pendingIntent){

        // notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager == null){
            return;
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_message)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(objectId.hashCode(), notification);

    }





}
