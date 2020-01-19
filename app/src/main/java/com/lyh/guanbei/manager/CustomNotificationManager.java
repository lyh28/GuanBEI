package com.lyh.guanbei.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.lyh.guanbei.R;
import com.lyh.guanbei.ui.activity.InputActivity;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CustomNotificationManager {
    private static final String INPUT_CHANNEL_ID = "input";
    private static final String INPUT_CHANNEL_NAME = "快捷输入";
    public static void init(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(new Intent(context, CustomNotificationManager.NotificationService.class));
        else
            context.startService(new Intent(context, CustomNotificationManager.NotificationService.class));
    }
    private static NotificationCompat.Builder getInputNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(INPUT_CHANNEL_ID, INPUT_CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, INPUT_CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
            builder.setPriority(NotificationCompat.PRIORITY_MIN);
        }
        //跳转
        Intent i = new Intent(context, InputActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteView.setOnClickPendingIntent(R.id.notification_layout, pendingIntent);
        builder.setContent(remoteView);
        builder.setSmallIcon(R.mipmap.icon_more_operation_share_friend);
        return builder;
    }

    public static class NotificationService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            NotificationCompat.Builder builder = getInputNotification(this);
            startForeground(100, builder.build());
            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
