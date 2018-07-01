package com.example.rutbiton.zeeksrorertest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class invoiceDetailsActivity extends AppCompatActivity {

    private TextView storeTxt, sumTxt, categoryTxt, invoiceDateTxt, dueDateTxt;
    private ImageView invoiceImage;
    private int invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        getDetails();
        FloatingActionButton fabC = (FloatingActionButton) findViewById(R.id.btnCheckInvoice);
        fabC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(invoiceDetailsActivity.this);
                dialog.setTitle("Are Tou Sure?")
                        .setIcon(R.drawable.ic_ques)
                        .setMessage("Do You Want Delete this doc?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                MainActivity.sqLiteHelper.deleteData(invoiceId);

                                Intent in = new Intent(invoiceDetailsActivity.this, homeFilesActivity.class);
                                startActivity( in );
                                finish();
                            }
                        }).show();
            }
        });
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void getDetails()
    {
        try {
            Bundle extras = getIntent().getExtras();
            invoiceId = extras.getInt("id");
            String store = extras.getString("store");
            String sum = extras.getString("sum");
            String Idate = extras.getString("date");
            String category = extras.getString("category");
            String isCredit = extras.getString("isCredit");
            String dueDate = extras.getString("dueDate");
            byte[] img = extras.getByteArray("img");
            // insert to views
            storeTxt.setText(store);
            sumTxt.setText("Sum: " + sum);
            categoryTxt.setText("Category: " + category);
            invoiceDateTxt.setText("Date: " + Idate);
            if (isCredit.equals("true")) {
                //it is credit
                dueDateTxt.setText("Expiry: " + dueDate);
            } else {
                //it is invoice
                dueDateTxt.setVisibility(View.GONE);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            invoiceImage.setImageBitmap(bitmap);


        } catch (Exception e) {
            Intent in = new Intent(invoiceDetailsActivity.this, homeFilesActivity.class);
            startActivity( in );
            finish();
        }

    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void init()
    {
        storeTxt = (TextView) findViewById(R.id.txtDetailsStore);
        sumTxt = (TextView) findViewById(R.id.txtDetailsSum);
        categoryTxt = (TextView) findViewById(R.id.txtDetailsCategory);
        invoiceDateTxt = (TextView) findViewById(R.id.txtDetailsDate);
        dueDateTxt = (TextView) findViewById(R.id.txtDetailsDueDate);
        invoiceImage = (ImageView) findViewById(R.id.imageViewDatails);
    }
}