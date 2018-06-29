package com.example.rutbiton.zeeksrorertest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class invoiceDetailsActivity extends AppCompatActivity {

    TextView storeTxt, sumTxt, categoryTxt, invoiceDateTxt, dueDateTxt;

    ImageView invoiceImage;
    int invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
              /*  AlertDialog alertDialog = new AlertDialog.Builder(invoiceDetailsActivity.this).create();
                alertDialog.setTitle("Are Tou Sure?");
                alertDialog.setMessage();"Do You Want Delete this doc?"
                alertDialog.setIcon(R.drawable.ic_c);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.sqLiteHelper.deleteData(invoiceId);
                                dialog.dismiss();
                                Intent in = new Intent(invoiceDetailsActivity.this, homeFilesActivity.class);
                                startActivity(in);
                                finish();
                                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

                AlertDialog.Builder dialog = new AlertDialog.Builder(invoiceDetailsActivity.this);
                dialog.setTitle( "Are Tou Sure?")
                        .setIcon(R.drawable.ic_ques)
                        .setMessage("Do You Want Delete this doc?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                                  }})
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                MainActivity.sqLiteHelper.deleteData(invoiceId);

                                Intent in = new Intent(invoiceDetailsActivity.this, homeFilesActivity.class);
                                startActivity(in);
                                finish();
                                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });
    }

    private  void getDetails(){
        try {
            Bundle extras = getIntent().getExtras();
            invoiceId = extras.getInt("id");
            String store = extras.getString("store");
            String sum = extras.getString("sum");
            String Idate = extras.getString("date");
            String category = extras.getString("category");
            String isCredit = extras.getString("isCredit");
            String dueDate = extras.getString("dueDate");
            byte [] img = extras.getByteArray("img");
            // insert to views
            storeTxt.setText(store);
            sumTxt.setText("Sum: "+sum);
            categoryTxt.setText("Category: "+category);
            invoiceDateTxt.setText("Date: "+Idate);
            if(isCredit.equals("true")){
                //it is credit
                //  dueDateTxt.setText("Dua Date: Not Inserted");
                dueDateTxt.setText("Dua Date: "+dueDate);
            }
            else{
                //it is invoice
                dueDateTxt.setVisibility(View.GONE);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            invoiceImage.setImageBitmap(bitmap);
         // invoiceImage.setTag(position);

        }catch (Exception e){
            Intent in = new Intent(invoiceDetailsActivity.this, homeFilesActivity.class);
            startActivity(in);
        }

    }

    private void init(){
        storeTxt = (TextView)findViewById(R.id.txtDetailsStore);
        sumTxt  = (TextView)findViewById(R.id.txtDetailsSum);
        categoryTxt = (TextView)findViewById(R.id.txtDetailsCategory);
        invoiceDateTxt = (TextView)findViewById(R.id.txtDetailsDate);
        dueDateTxt= (TextView)findViewById(R.id.txtDetailsDueDate);

        invoiceImage = (ImageView) findViewById(R.id.imageViewDatails);
    }
}
