package com.example.rutbiton.zeeksrorertest;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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