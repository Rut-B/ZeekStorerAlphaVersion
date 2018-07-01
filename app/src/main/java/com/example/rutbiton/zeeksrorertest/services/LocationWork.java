package com.example.rutbiton.zeeksrorertest.services;

import android.database.Cursor;
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

    public WorkerResult doWork() {

        Log.e("INWORK", "doWork: Started to work1111111111111111111111111111111111111111111111111111111111111" + counter);
     zeekNotification  zeek_notification = new zeekNotification(getApplicationContext());
        sqLiteHelper = new SQLiteHelper(getApplicationContext(), "InvoiceDB.sqlite", null, 1);
        // Do the work here--in this case,
        //This method will run on background thread

        //CHECK AND SHOE NOTI IF THERE IS ACTUAL LOCATION- CREDIT
//        Location targetLocation = new Location("");//provider name is unnecessary
//        targetLocation.setLatitude(35);//your coords of course
//        targetLocation.setLongitude(33);
//        GPSserviceActivity.onLocationChangedNames(targetLocation,getApplicationContext());

        //CHECK AND SHOE NOTI IF THERE IS ACTUAL LOCATION- DATE
       String actualStores = this.getCreditListDueDatesGoOver();
        if (!actualStores.equals(""))
        {
            zeek_notification.sendNotification("The following credits are about to expire: ",actualStores,35);
        }
       Log.e("worker", actualStores);
        // Indicate success or failure with your return value:
        return WorkerResult.SUCCESS;


    }



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






}