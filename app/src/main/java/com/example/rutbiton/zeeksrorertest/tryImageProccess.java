package com.example.rutbiton.zeeksrorertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class tryImageProccess extends AppCompatActivity {

    ImageView imageview;
    Button btnProcess;
    TextView txtView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_image_proccess);

        imageview = findViewById(R.id.image_view);
        btnProcess = findViewById(R.id.btnProcess);
        txtView = findViewById(R.id.txtView);
        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ex2);
        imageview.setImageBitmap(bitmap);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!txtRecognizer.isOperational()) {
                    txtView.setText("error!");
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append("/");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());

                            }
                        }
                    }
                    txtView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
                }
            }
        });
    }
}