package com.example.rutbiton.zeeksrorertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InvoiceAdapter extends ArrayAdapter<Invoice> /*implements View.OnClickListener*/{

private ArrayList<Invoice> dataSet;
        Context mContext;

// View lookup cache
private static class ViewHolder {
    TextView txtStore;
    TextView txtSum;
    TextView txtDate;
    TextView txtCategory;
    ImageView info;
    ImageView icon;
}



    public InvoiceAdapter(ArrayList<Invoice> data, Context context) {
        super(context, R.layout.activity_credit_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

/*
    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Invoice dataModel=(Invoice)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }*/

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Invoice dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_credit_item, parent, false);
            viewHolder.txtStore = (TextView) convertView.findViewById(R.id.txtStore);
          //  viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
           // viewHolder.txtSum = (TextView) convertView.findViewById(R.id.txtSum);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.imgDoc);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.imgItemIcon);
            viewHolder.txtCategory= (TextView) convertView.findViewById(R.id.txtCategory);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtStore.setText(dataModel.getStore());
       // viewHolder.txtSum.setText(dataModel.getSum());
        //viewHolder.txtDate.setText(dataModel.getDate());
        viewHolder.txtCategory.setText(dataModel.getCategory());
        byte[] invoiceImage = dataModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(invoiceImage, 0, invoiceImage.length);
        viewHolder.info.setImageBitmap(bitmap);
        viewHolder.info.setTag(position);
        if(dataModel.getIsCredit().equals("true"))
            viewHolder.icon.setImageResource(R.drawable.green);
        else
            viewHolder.icon.setImageResource(R.drawable.red2);
        // Return the completed view to render on screen
        return convertView;
    }


}
