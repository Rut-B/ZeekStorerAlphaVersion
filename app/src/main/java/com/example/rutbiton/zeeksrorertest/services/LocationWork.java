package com.example.rutbiton.zeeksrorertest.services;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.rutbiton.zeeksrorertest.GPSserviceActivity;
import com.example.rutbiton.zeeksrorertest.SQLiteHelper;
import com.example.rutbiton.zeeksrorertest.zeekNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import androidx.work.Worker;

public class LocationWork extends Worker {
public static final int DAYS_BEFORE_DUE = 7;
public static String TAG="in worker";
public static int counter = 0;
public static SQLiteHelper sqLiteHelper;
//private GPSserviceActivity mservice;

public WorkerResult doWork() {

        Log.e("INWORK", "doWork: Started to work1111111111111111111111111111111111111111111111111111111111111" + counter);
        zeekNotification  zeek_notification = new zeekNotification(getApplicationContext());
        sqLiteHelper = new SQLiteHelper(getApplicationContext(), "InvoiceDB.sqlite", null, 1);
       String actualStores = this.getCreditListDueDatesGoOver();
        if (!actualStores.equals(""))
        {
            zeek_notification.sendNotification("The following credits are about to expire: ",actualStores,35);
        }
       Log.e("worker", actualStores);
        // Indicate success or failure with your return value:
        return WorkerResult.SUCCESS;


    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public static ArrayList<String> getCreditListPlaces(){
        //System.out.println("\n_______tי___________    in func    __________\n");
        ArrayList<String> stores = new ArrayList<>();
        Cursor cursor = LocationWork.sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true'");
        while (cursor.moveToNext()) {

            String store = cursor.getString(1);
            stores.add(store);
            System.out.println("\n____________________________\n"+store);
        }


        return stores;
    }
    public static String getCreditListDueDatesGoOver(){
        //System.out.println("\n_______tי___________    in func    __________\n");
        String stores = "";
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true' AND  dueDate != 'Not Inserted'");
        while (cursor.moveToNext()) {
            Date date;
            String store = cursor.getString(1);
            String dueDate = cursor.getString(7);
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try{
                date = format.parse(dueDate);
                Calendar c = Calendar.getInstance();
                Date currctDate = c.getTime();
                c.add(Calendar.DATE, DAYS_BEFORE_DUE);
                Date nextWeekDate = c.getTime();
                if(date.after(currctDate) && date.before(nextWeekDate)) {//if is till 6 days next
                    // In between
                    //stores.add(store);
                    stores = stores + store + ", ";
                }
                 else if(date.equals(currctDate)) {
                    //due date is today
                    stores = stores + store;
                }
            }catch (Exception e){

            }
            System.out.println(dueDate);

            System.out.println("\n____________________________\n"+store);
        }


        return stores;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    private void enable_service()
//    {
//
//        Intent i = new Intent(getApplicationContext(), mservice.getClass());
//        if (!isMyServiceRunning(mservice.getClass())) {
//            getApplicationContext().startService(i);
//        }
//    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~




}