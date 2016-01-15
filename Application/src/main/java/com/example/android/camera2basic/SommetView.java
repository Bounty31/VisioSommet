package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Omar on 15/01/2016.
 */
public class SommetView extends LinearLayout {
    ImageView image;
    TextView tvName;
    TextView distanceTo;
    public SommetView(Context context,int imageID, String nom,float distance) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        image = new ImageView(context);
        tvName = new TextView(context);
        distanceTo = new TextView(context);
        image.setImageResource(imageID);
        tvName.setText(nom);
        distanceTo.setText(distance + "");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 200);
        image.setLayoutParams(layoutParams);
        this.addView(image);
        this.addView(tvName);
        this.addView(distanceTo);
        tvName.setSingleLine();
        tvName.setGravity(Gravity.CENTER);
        tvName.setTextColor(Color.WHITE);

        distanceTo.setSingleLine();
        distanceTo.setGravity(Gravity.CENTER);
        distanceTo.setTextColor(Color.WHITE);
    }

    public void updateDistance(float distance) {
        double d = distance;
        DecimalFormat format = new DecimalFormat( "##.##" );
        distanceTo.setText(format.format(d)+"km");
        float taille = 5/distance;

        if(taille<0.3f)
            taille = 0.3f;
        if(taille>1)
            taille = 1;


       setScaleX(taille);
        setScaleY(taille);
    }
}
