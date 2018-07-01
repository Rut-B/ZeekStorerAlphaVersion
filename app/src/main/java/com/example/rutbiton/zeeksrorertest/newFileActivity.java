package com.example.rutbiton.zeeksrorertest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class newFileActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);
        imageView = (ImageView)findViewById(R.id.imageViewDatails);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        //have to add data to this image!
        imageView.setImageBitmap(bitmap);

        // move to get info
        Intent in = new Intent(newFileActivity.this, TempActivity.class);
        Bundle b = new Bundle();
        b.putByteArray("image", ImageHandler.imageViewToByte(imageView)); //
        in.putExtras(b); //Put your image to your next Intent
        startActivity(in);
        finish();
        }
        else
        {
            finish();
        }
    }
}











