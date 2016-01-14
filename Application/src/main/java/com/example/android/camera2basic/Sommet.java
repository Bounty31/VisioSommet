package com.example.android.camera2basic;

/**
 * Created by ozeroual on 14/01/16.
 */
public class Sommet {

    private int id;
    private float latitude;
    private float longitude;
    private float altitude;
    private String nom;
    public float angle;
    public boolean angleSet = false;

    public Sommet(int id, float latitude, float longitude, float altitude, String nom) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.nom = nom;
    }

    public Sommet(float latitude, String nom, float altitude, float longitude) {
        this.latitude = latitude;
        this.nom = nom;
        this.altitude = altitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAngle(float angle) {
        this.angleSet = true;
        this.angle = angle;
    }
}
