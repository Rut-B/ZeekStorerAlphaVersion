package com.example.rutbiton.zeeksrorertest;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.rutbiton.zeeksrorertest.services.ScheduleClient;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private int SPLASH_TIME_OUT = 1500;
    public static SQLiteHelper sqLiteHelper;
    public static Address my_current_address;
    public static Geocoder geocoder;
    private GPSserviceActivity mservice;
    private TempService mSensorService;

    public String ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        sqLiteHelper = new SQLiteHelper(this, "InvoiceDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS INVOICE(Id INTEGER PRIMARY KEY AUTOINCREMENT, store VARCHAR, sum VARCHAR, image BLOB,date VARCHAR,category VARCHAR,isCredit VARCHAR, dueDate VARCHAR)");

        mservice = new GPSserviceActivity();
        mSensorService = new TempService();
        if (!runtime_permissions())
            enable_service();

        Intent mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"1")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...");

        NotificationManager mNotificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

       new Handler().postDelayed(new Runnable() {
           @Override
            public void run() {
                Intent in = new Intent(MainActivity.this, homeFilesActivity.class);
                startActivity(in);
               finish();
            }
        }, SPLASH_TIME_OUT);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     private void enable_service()
     {
         Intent i = new Intent(getApplicationContext(), mservice.getClass());
         if (!isMyServiceRunning(mservice.getClass())) {
             startService(i);
         }
     }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private boolean runtime_permissions()
    {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
                return true;
            }
            return false;
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == 100){
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    enable_service();
                }else {
                    runtime_permissions();
                }
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onResume()
    {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(MainActivity.this, "This is my Toast message!"+"\n" +intent.getExtras().get("coordinates"),
                            Toast.LENGTH_LONG).show();
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static ArrayList<String> getCreditListPlaces()
    {
        //System.out.println("\n__________________    in func    __________\n");
        ArrayList<String> stores = new ArrayList<>();
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true'");
        while (cursor.moveToNext()) {

            String store = cursor.getString(1);
            stores.add(store);
            System.out.println("\n____________________________\n"+store);
        }


        return stores;
    }

}
