package com.example.rutbiton.zeeksrorertest;


import android.app.Notification;
import android.content.Context;


public class zeekNotification {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NOTI_PRIMARY1 = 1100;
    private static final int NOTI_PRIMARY2 = 1101;
    private static final int NOTI_SECONDARY1 = 1200;
    private static final int NOTI_SECONDARY2 = 1201;
    private NotificationHelper noti;

    public zeekNotification(Context ctx) {
        noti = new NotificationHelper(ctx);
    }

    public void sendNotification(String title, String body,int idFile) {
        Notification.Builder nb = null;

        nb = noti.getNotification2(title, body);
        if (nb != null) {
            noti.notify(idFile, nb);
        }
    }

}