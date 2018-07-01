package com.example.rutbiton.zeeksrorertest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

//import com.example.rutbiton.zeeksrorertest.services.ScheduleClient;

import de.keyboardsurfer.android.widget.crouton.Crouton;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TempActivity extends AppCompatActivity {

    EditText edtStore, edtSum;//, edtDate;
    Button btnSave;
    ImageView imageView;
    DatePicker dueDate;
    CheckBox haveDueDate;
    Spinner spinner;
    char kind; //'i' = invoice , 'c' = credit
    ChipGroup chipGroup;
    Chip invC, creC;
    // This is a handle so that we can call methods on our service


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        init();




        //get image
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("image");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.imageViewDatails);

        image.setImageBitmap(bmp);
       haveDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(haveDueDate.isChecked()){
                    dueDate.setVisibility(View.VISIBLE);
                }
                else{
                    dueDate.setVisibility(View.GONE);
                }
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dueDateStr;
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());//have to get date
              if(haveDueDate.isChecked()) {
                  dueDateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(getDateFromDatePicker(dueDate));
              }
              else{
                 dueDateStr = "Not Inserted";

              }
               try{
                    MainActivity.sqLiteHelper.insertData(
                            edtStore.getText().toString().trim().toLowerCase(),
                            edtSum.getText().toString().trim(),
                            date,
                    //        ,
                           ImageHandler.imageViewToByte(imageView),
                            spinner.getSelectedItem().toString().toLowerCase(),
                            ""+isCredit(),
                            dueDateStr
                    );
                   Log.d("tempA", "insert to sql");
                  //  Toast.makeText(getApplicationContext(), "Added successfully!"+isCredit()+dueDateStr, Toast.LENGTH_SHORT).show();
                    //View customView = getLayoutInflater().inflate(R.layout.custom_crouton_layout, null);
                  //  Crouton.show(TempActivity.this, customView);
                    Intent in = new Intent(TempActivity.this, InvoiceListActivity.class);


                    Bundle b = new Bundle();
                    b.putString("m","add"); //
                    in.putExtras(b);
                    if(kind=='i'){
                        Bundle k = new Bundle();
                        k.putString("option","invoice"); //
                        in.putExtras(k);
                    }
                    else if(kind=='c'){
                        Bundle k = new Bundle();
                        k.putString("option","credit"); //
                        in.putExtras(k);
                    }
                    else{
                        Bundle k = new Bundle();
                     //   Toast.makeText(getApplicationContext(), "latest!", Toast.LENGTH_LONG).show();
                        k.putString("option","latest"); //
                        in.putExtras(k);
                    }
                    startActivity(in);
                    finish();
                   /* edtStore.setText("");
                    edtSum.setText("");
                    edtSum.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);*/
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("tempA", "in exeption");
                   Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        this.kind = 'i';
       invC.setChecked(true);
        invC.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                // Handle the toggle.
                if(isChecked){
                    kind = 'i';
                   // Toast.makeText(getApplicationContext(), "iiiiiiiiiiiiiiiiiiiiiiiii", Toast.LENGTH_LONG).show();
                    creC.setChecked(false);
                }
            }
        });
        creC.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                // Handle the toggle.
                if(isChecked){
                    kind = 'c';
                 //   Toast.makeText(getApplicationContext(), "ccccccccccccccccccccccccccccc", Toast.LENGTH_LONG).show();
                    invC.setChecked(false);
                }
            }
        });

    }



    private boolean isCredit(){return kind =='c';}


    private void init(){
      //  edtDate = (EditText) findViewById(R.id.edtDay);
        dueDate = (DatePicker) findViewById(R.id.dueDate);
        edtStore = (EditText)findViewById(R.id.edtStore) ;
        edtSum = (EditText)findViewById(R.id.edtSum) ;
        imageView = (ImageView) findViewById(R.id.imageViewDatails);
        haveDueDate = (CheckBox) findViewById(R.id.checkDueDate);
        btnSave = (Button)findViewById(R.id.btnSave);


        spinner = (Spinner) findViewById(R.id.categorySpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        invC = (Chip)findViewById(R.id.chipInvoice);
        creC = (Chip)findViewById(R.id.chipCredit);
         chipGroup = (ChipGroup) findViewById(R.id.chipGroup);

    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
Log.d("tempA", "get due date");
        return calendar.getTime();
    }
}
