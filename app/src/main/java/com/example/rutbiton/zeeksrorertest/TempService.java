package com.example.rutbiton.zeeksrorertest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class TempService extends Service {
    public int counter=0;
    private String NOTIFI_TITLE = "you can use a credits of:\n";
    zeekNotification zeek_notification;
    SensorRestarterBroadcastReceiver reciver;
//    public SensorService() {
//        super();
//        Log.i("HERE", "here I am!");
//    }

    public TempService() {
    }
    public void onCreate() {
        super.onCreate();



        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction("com.example.rutbiton.zeeksrorer.ActivityRecognition.RestartSensor");

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);

        // Create a network change broadcast receiver.
        reciver = new SensorRestarterBroadcastReceiver();

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(reciver, intentFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("com.example.rutbiton.zeeksrorer.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();

    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 3000, 3000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        Log.d("n b*************************************************mkjkm", "nhgcgvhg===========================================================jhjhjh");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                zeek_notification = new zeekNotification(getApplicationContext());
                Log.d("n bh///////////////////////////////////////////////////////////////////mkmkjkm", "nhgcgvhg===========================================================jhjhjh");
                zeek_notification.sendNotification(NOTIFI_TITLE,"timer"+counter,33);
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
