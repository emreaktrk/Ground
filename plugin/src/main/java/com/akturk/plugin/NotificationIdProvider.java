package com.akturk.plugin;

public final class NotificationIdProvider {
    private static int NOTIFICATION_ID = 4321;

    public static int getNotificationId() {
        NOTIFICATION_ID = NOTIFICATION_ID++;
        return NOTIFICATION_ID;
    }
}