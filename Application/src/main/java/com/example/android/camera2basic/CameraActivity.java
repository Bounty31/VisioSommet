/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CameraActivity extends Activity implements LocationListener {

    final int PERMISSION_LOCATION = 3;
    private double latitude = -1;
    private double longitude = -1;
    float angleBear = 0;
    private Location locationToBear = null;

    OrientationPrecise mOrientationPrecise = null;
    BroadcastReceiver br_orientation = null;
    String orientation = "";
    private ImageView image;

    double azimut = 0;
    double pitch = 0;
    double roll = 0;

    int widthDevice = 0;
    int heightDevice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        latitude = 47.642787;
        longitude = 6.8397398;
        locationToBear = new Location("LocationToBear");
        locationToBear.setLongitude(8.569485);
        locationToBear.setLatitude(47.345272);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }


        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Cela signifie que la permission à déjà était
                    //demandé et l'utilisateur l'a refusé
                    //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                    //cette permission est nécessaire et la redemander
                } else {
                    //Sinon demander la permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_LOCATION);
                }
            } else {
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = this;
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);
            }
        } else {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = this;
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);

        }

        mOrientationPrecise = OrientationPrecise.getOrientationPrecise(this);

        br_orientation = new BroadcastReceiver() {
            String pattern = "##.##";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            public void onReceive(Context context, Intent intent) {
                orientation = "Azimuth: " + decimalFormat.format(intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_0, -9));
                orientation += " Pitch: " + decimalFormat.format(intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_1, -9));
                orientation += " Roll: " + decimalFormat.format(intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_2, -9));
                azimut = intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_0, -9);
                pitch = intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_1, -9);
                roll = intent.getFloatExtra(OrientationPrecise.CLEF_ORIENTATION_2, -9);
                updateUI();
            }
        };

        GestionnaireBaseSommets gbs = GestionnaireBaseSommets.getGestionnaireBaseSommets(getApplicationContext());
        if (!GestionnaireBaseSommets.instanciee && !GestionnaireBaseSommets.initialisee)
            gbs.insertinitialisation();

        int nbsommets = gbs.combienDeSommets();
        Toast.makeText(this, "" + nbsommets, Toast.LENGTH_LONG).show();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        heightDevice = displaymetrics.heightPixels;
        widthDevice = displaymetrics.widthPixels;

        gbs.getAll();

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        angleBear = convertToRadianLocation(location.bearingTo(locationToBear));
        Log.d("Location", "" + longitude + "," + latitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);

                    LocationListener locationListener = this;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);
                } else {
                    // La permission est refusée
                }
                return;
            }
        }
    }

    private void updateUI() {
        image = (ImageView) findViewById(R.id.imageView);
        double angleFocal = (Math.PI * 60 / 180);
        double pasParRadian = widthDevice / angleFocal;
        //Azimut = 0 quand le telephone pointe le nord, mais azimut = 0- pi/2 quand le telephone fait FACE au nord
        float posX = 0;


        if (roll > 0) {
            double nordTelephone = -Math.PI/2 + Math.PI - angleBear;
            if (azimut <= nordTelephone + angleFocal / 2 && azimut >= nordTelephone - angleFocal / 2) {
            }
            posX = (float) (widthDevice / 2 + (azimut - nordTelephone) * pasParRadian) - image.getWidth()/2;
            if(angleBear!=0 && posX<0)
                posX*=-1;
            image.setX(posX);

            image.setY(heightDevice - image.getHeight());
            image.setRotation(0);
            Log.d("Posx", nordTelephone+"");

        } else {
            double nordTelephone = -Math.PI / 2 - angleBear;
            if (azimut <= nordTelephone + angleFocal / 2 && azimut >= nordTelephone - angleFocal / 2) {

            }
            posX = (float) (widthDevice / 2 - (azimut - nordTelephone) * pasParRadian) - image.getWidth()/2;

            image.setX(posX);

            image.setY(0);
            image.setRotation(180);
        }
        //verification que l'objet est dans l'angle focal

//90*cos(x/(1920-25)*3.14)
        //si le nord est à droite de l'écran
        if (image.getX() > widthDevice - image.getWidth() / 2) {
            image.setX(widthDevice - image.getWidth());
            //image.setRotation(-90);
        }
        //si le nord est à gauche de l'écran
        else if (image.getX() < 0 - image.getWidth() / 2) {
            image.setX(0);
            //image.setRotation(90);
        }
        float angleAiguille = 0;
        if (roll > 0) {
            angleAiguille = -(float) Math.toDegrees(Math.cos(image.getX() / (widthDevice - image.getWidth()) * Math.PI));
            image.setRotation(angleAiguille+180);
        }
        else {
            angleAiguille = (float) Math.toDegrees(Math.cos(image.getX() / (widthDevice - image.getWidth()) * Math.PI));
            image.setRotation(angleAiguille);
        }
        Log.d("Angle", "" + angleAiguille);

        // Log.d("Pitch : ",""+roll);
        // set la position de la boussole indiquant le nord, en haut si le nord est en face, en bas si le nord est à l'opposé
        //Log.d("Orientation",orientation);
        // Log.d("Orientation",""+roll);

    }

    public float convertToRadianLocation(float deg) {
        return -(float) (deg * Math.PI / 180);
    }

    protected void onPause() {
        super.onPause();
        mOrientationPrecise.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(br_orientation, new IntentFilter(OrientationPrecise.MESSAGE_ORIENTATION));
        mOrientationPrecise.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br_orientation);
    }

    public float modulo(float nb, float modulo) {
        float resultat = nb;
        if (resultat > modulo) {
            while (resultat > modulo) {
                resultat -= modulo;
            }
            if (resultat < 0)
                resultat += modulo;
        } else if (resultat < 0) {
            while (resultat < 0) {
                resultat += modulo;
            }
        }
        return resultat;
    }

}


