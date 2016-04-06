package com.akturk.plugin;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

public final class NotificationFacade {
    private Context mContext;
    private Notification.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public NotificationFacade(Context context) {
        mContext = context;
        mBuilder = new Notification.Builder(context);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public Notification.Builder getBuilder() {
        return mBuilder;
    }

    public void show() {
        int notificationId = NotificationIdProvider.getNotificationId();
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}