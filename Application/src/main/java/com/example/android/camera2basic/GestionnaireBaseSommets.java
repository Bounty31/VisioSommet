package com.example.android.camera2basic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.camera2basic.RequeteCreationSommets;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by couchot on 02/10/15.
 */
public class GestionnaireBaseSommets extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db_sommets";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_SOMMET = "sommets";
    public static final String ID = "id";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String NOM = "nom";
    public static final String ALTITUDE = "altitude";

    public static boolean instanciee = false;
    public static boolean initialisee = false;
    public static GestionnaireBaseSommets gbs = null;

    public static GestionnaireBaseSommets getGestionnaireBaseSommets(Context context) {
        if (!instanciee) {
            gbs = new GestionnaireBaseSommets(context);
            instanciee = true;
        }
        return gbs;
    }

    private GestionnaireBaseSommets(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SOMMETS = "CREATE TABLE " + TABLE_SOMMET + "("
                + ID + " INTEGER PRIMARY KEY,"
                + LONGITUDE + " TEXT,"
                + LATITUDE + " TEXT,"
                + NOM + " TEXT,"
                + ALTITUDE + " INTEGER)";
        db.execSQL(CREATE_TABLE_SOMMETS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOMMET);
        onCreate(db);
        initialisee = false;
    }


    public void initialisationAvecQuelquesSommets() {
        if (initialisee == false) {
            String reqInsertion4Sommets[] = {
                    "INSERT INTO sommets VALUES (1116621810, '6.84486789904718', '47.8224908948974', 'Ballon d''Alsace', 1247);",
                    "INSERT INTO sommets VALUES (1661447505, '6.89760629903985', '47.8895027948947', 'Col de Bussang', 731);",
                    "INSERT INTO sommets VALUES (1662823209, '6.91711559903713', '47.9227979948934', 'Col d''Oderen', 884);",
                    "INSERT INTO sommets VALUES (1708009463, '6.92187929903647', '47.9388803948927', 'Les Winterges', 1049);"};
            SQLiteDatabase db = this.getWritableDatabase();
            for (String s : reqInsertion4Sommets) {
                db.execSQL(s);
            }
            db.close(); // Closing database connection
            initialisee = true;
        }
    }

    public int combienDeSommets() {
        String countQuery = "SELECT  * FROM " + TABLE_SOMMET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c = cursor.getCount();
        db.close();
        // return count
        return c;
    }

    public void insertinitialisation() {
        String reqInsertion4Sommets[] = RequeteCreationSommets.Requetes;
        SQLiteDatabase db = this.getWritableDatabase();
        for (String s : reqInsertion4Sommets) {
            db.execSQL(s);
        }
        db.close(); // Closing database connection
        initialisee = true;
    }


    public ArrayList<Sommet> getAll() {
        ArrayList<Sommet> resultat = new ArrayList<Sommet>();

        String countQuery = "SELECT  * FROM " + TABLE_SOMMET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)));
                float latitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                float longitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                float altitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex(ALTITUDE)));
                String nom = cursor.getString(cursor.getColumnIndex(NOM));

                Sommet newSommet = new Sommet(id,latitude,longitude,altitude,nom);
                resultat.add(newSommet);
                cursor.moveToNext();
            }
        }
            return resultat;
        }

    /*
        public GestionnaireBaseSommets(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SOMMETS = "CREATE TABLE " + TABLE_SOMMET + "("
                + ID + " INTEGER PRIMARY KEY,"
                + LONGITUDE + " TEXT,"
                + LATITUDE  + " TEXT,"
                + NOM + " TEXT,"
                + ALTITUDE  + " INTEGER)";
        db.execSQL(CREATE_TABLE_SOMMETS);

        RemplissageDesSommetsAsyncTask rst = new RemplissageDesSommetsAsyncTask(this,mContext);
        rst.execute(RequeteCreationSommets.Requetes);
    }

     */

}
