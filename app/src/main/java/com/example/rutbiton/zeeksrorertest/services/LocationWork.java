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
import com.example.rutbiton.zeeksrorertest.MainActivity;
import com.example.rutbiton.zeeksrorertest.SQLiteHelper;
import com.example.rutbiton.zeeksrorertest.zeekNotification;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


import androidx.work.Worker;

public class LocationWork extends Worker {
public static final int DAYS_BEFORE_DUE = 7;
public static String TAG="in worker";
public static int counter = 0;
public static SQLiteHelper sqLiteHelper;


    public WorkerResult doWork()
    {

        zeekNotification  zeek_notification = new zeekNotification(getApplicationContext());
        sqLiteHelper = new SQLiteHelper(getApplicationContext(), "InvoiceDB.sqlite", null, 1);
        String actualStores = this.getCreditListDueDatesGoOver();

         if (!actualStores.equals(""))
        {
            zeek_notification.sendNotification("The following credits are about to expire: ",actualStores,35);
        }
        // Indicate success or failure with your return value:
        return WorkerResult.SUCCESS;
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static ArrayList<String> getCreditListPlaces()
    {
        ArrayList<String> stores = new ArrayList<>();
        Cursor cursor = LocationWork.sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true'");
        while (cursor.moveToNext())
        {
            String store = cursor.getString(1);
            stores.add(store);
        }
        return stores;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static String getCreditListDueDatesGoOver()
    {
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
                    stores = stores + store + " , ";
                }
                 else if(compare(date,currctDate)==0) {
                    //due date is today
                    stores = stores +store + " , " ;
                }
            }catch (ParseException e){
                Log.e(MainActivity.TAG_APP,"ParseException in getCreditListDueDatesGoOver func  ");
            }
        }
        return stores;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static int compare(Date d1, Date d2)
    {
            if (d1.getYear() != d2.getYear())
                return d1.getYear() - d2.getYear();
            if (d1.getMonth() != d2.getMonth())
                return d1.getMonth() - d2.getMonth();
            return d1.getDate() - d2.getDate();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}