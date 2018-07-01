package com.example.rutbiton.zeeksrorertest;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class InvoiceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    ArrayList<Invoice> list;
    ListView listView;
    LinearLayout searchPart;
    ImageButton startSearch;
    EditText searchText;
    RadioGroup radioG;
    RadioButton radioStore, radioCategory;
    private static InvoiceAdapter adapter;
    private Cursor cursor;
    private boolean searchByStore;

    @Override
    protected void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invoice_list);
        listView = (ListView) findViewById(R.id.listIn);
        searchPart = (LinearLayout) findViewById(R.id.search_part);
        startSearch = (ImageButton)findViewById(R.id.btnStartSearch);
        searchText = (EditText)findViewById(R.id.edtSearch);
        radioG = (RadioGroup)findViewById(R.id.radioGroup1);
        radioStore = (RadioButton)findViewById(R.id.radioStore);
        radioStore.setSelected(true);
        radioCategory = (RadioButton)findViewById(R.id.radioCategory);
        radioCategory.setSelected(false);
        listView.setOnItemClickListener(this);
        getMassages();

        searchByStore = true;
        cursor = getOption();
        getListItems(cursor);
        list = new ArrayList<>();
        radioCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              searchByStore = false;
            }
        });
        radioStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchByStore = true;
            }
        });
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //have to get search text, get if is category or store name- send to search and show in list
                String str = searchText.getText().toString().trim().toLowerCase();
                String[] arguments= new String[]{str};

                if(searchByStore){
                    //have searc by store name
                    Cursor cur = MainActivity.sqLiteHelper.getDataWithParams("SELECT * FROM INVOICE WHERE store=?", arguments);
                    getListItems(cur);
                    listView.setVisibility(View.VISIBLE);
                }
                else if (!searchByStore){
                    //have to search by category
                    Cursor cur = MainActivity.sqLiteHelper.getDataWithParams("SELECT * FROM INVOICE WHERE category=?", arguments);
                    getListItems(cur);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void getListItems(Cursor cursor)
    {
        list = new ArrayList<>();
        // get all data from sqlite
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String store = cursor.getString(1);
            String sum = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String date = cursor.getString(4);
            String category = cursor.getString(5);
            String kind = cursor.getString(6);
            String dueDate = cursor.getString(7);

            list.add(new Invoice(date, store, sum, category, image,kind, dueDate, id));
        }
        Collections.reverse(list);
        if(list.isEmpty()){
            //show snake
            Snackbar.make(listView, "No Items in List", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();

        }
        adapter= new InvoiceAdapter(list,getApplicationContext());
            listView.setAdapter(adapter);
        }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void getMassages()
    {
        try {
            //get and show massage
        Bundle extras = getIntent().getExtras();
        String massage = extras.getString("m");
        }
        catch (Exception e){
        }
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private Cursor getOption()
    {
        try {
            //get and show massage

            Bundle extras = getIntent().getExtras();
            String option  = extras.getString("option");


           switch (option){
               case "invoice":{
                   searchPart.setVisibility(View.GONE);
                  return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='false'");
               }
               case "credit":{
                   searchPart.setVisibility(View.GONE);
                   return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE WHERE isCredit='true'");
               }
               case "latest":{
                   searchPart.setVisibility(View.GONE);
                   return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE   cast(date AS DATE) AS date ORDER BY date DESC LIMIT 5");
               }
               case "search":{
                   searchPart.setVisibility(View.VISIBLE);
                   listView.setVisibility(View.GONE);
                   return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE LIMIT 1");
               }
           }
        }
        catch (Exception e){

            return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE");
        }
        return MainActivity.sqLiteHelper.getData("SELECT * FROM INVOICE");
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        Invoice item = (Invoice) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent in = new Intent(this, invoiceDetailsActivity.class);
        Bundle b = new Bundle();
        b.putString("store",item.getStore()); //
        b.putString("sum",item.getSum());
        b.putString("category",item.getCategory());
        b.putString("date",item.getDate());
        b.putString("isCredit",item.getIsCredit());
        b.putString("dueDate",item.getDueDate());
        b.putByteArray("img",item.getImage());
        b.putInt("id", item.getId());
        in.putExtras(b);
        startActivity(in);
    }
    }
