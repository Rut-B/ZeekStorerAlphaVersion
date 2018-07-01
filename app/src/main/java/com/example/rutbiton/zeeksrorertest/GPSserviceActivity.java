package com.example.rutbiton.zeeksrorertest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
 * this class responsible to GPS service [Rut]
 *
 * */
public class GPSserviceActivity extends Service
{

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_NEARBY_SEARCH = "/nearbysearch";
    private static final String TYPE_SEARCH = "/findplacefromtext";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD1AhkL3xRtCejIj22i6A64AOVPG3_se-o";
    private static final String API_KEY_1 = "AIzaSyABdxe1D1D1o9xRFJkxSc8n3c_ZXK3oCrE";

    private static final int RADIUS = 500;
    private static final int MIN_DIST = 500;
    private static final int MIN_TIME = 10;
    private static final String STRING_STATUS = "\"status\" : \"OK\"" ;

    public static String NOTIFI_TITLE = "You can redeem your credits of:\n";
    public static SQLiteHelper sqLiteHelper;
    private zeekNotification zeek_notification;
    private LocationListener listener;
    private LocationManager locationManager=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    //this function is called one time when the application is install on phone..
    public void onCreate()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        zeek_notification = new zeekNotification(getApplicationContext());
        sqLiteHelper = new SQLiteHelper(this, "InvoiceDB.sqlite", null, 1);
        listener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {
            }

            @Override
            public void onProviderEnabled(String s)
            {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, listener);
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        listener = new LocationListener()
        {

            @Override
            public void onLocationChanged(Location location)
            {
                onLocationChangedNames(location,getApplicationContext());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return super.onStartCommand(intent, flags, startId);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, listener);

        return START_STICKY;

    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
        Intent broadcastIntent = new Intent("ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static boolean udateCreditNotifi(String name_store,Location location,Context context)
    {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        zeekNotification zeek_notification;
        zeek_notification = new zeekNotification(context);
        boolean existStore = false;

        try
        {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_NEARBY_SEARCH + OUT_JSON);
            sb.append("?location=" + location.getLatitude() + "," + location.getLongitude());
            sb.append("&radius=" + RADIUS);
            sb.append("&name=" + name_store);
            sb.append("&key=" + API_KEY_1);
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();

            if(conn==null)
            {
                return false;
            }

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            if(in==null)
            {
                return false;
            }
            int read;
            char[] buff = new char[10000];

            while ((read = in.read(buff)) != -1)
            {
                jsonResults.append(buff, 0, read);
            }

            try
            {
                String json_string =jsonResults.toString();
                JSONObject jsonObj = new JSONObject(json_string);
                int count = jsonObj.length();

                if(json_string.contains(STRING_STATUS))
                {
                    existStore = true;
                }
                else
                {
                    existStore = false;
                }

            } catch (JSONException e)
            {
                Log.e(MainActivity.TAG_APP, "onCreate :Cannot process JSON results");
            }
        } catch (MalformedURLException e)
        {
            Log.e(MainActivity.TAG_APP, "Error processing Places API URL");
        } catch (IOException e)
        {
            Log.e(MainActivity.TAG_APP, "Error connecting to Places API");
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return  existStore;
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void onLocationChangedNames(Location location,Context context)
    {
        List<String> placesList;
        String creditsListUp = "";
        placesList = GPSserviceActivity.getCreditListPlaces();
        creditsListUp="";
        boolean flag= false;
        zeekNotification zeek_notification;
        zeek_notification = new zeekNotification(context);

        for (int i = 0; i < placesList.size(); i++) {
            String name = placesList.get(i);
            flag = udateCreditNotifi(name,location,context);
            if(flag)
            {
                creditsListUp += name+" ,\n";
            }

        }
        if (!creditsListUp.equals(""))
        {
            zeek_notification.sendNotification(GPSserviceActivity.NOTIFI_TITLE,creditsListUp,33);
        }

    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static ArrayList<String> getCreditListPlaces()
    {
        ArrayList<String> stores = new ArrayList<>();
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true'");
        while (cursor.moveToNext()) {
            String store = cursor.getString(1);
            stores.add(store);
        }
        return stores;
    }
}
