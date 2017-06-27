package com.example.lydia.mobappprojekt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Lydia on 25.06.2016.
 * Hilfsklasse, die die Arbeit mit der Datenbank vereinfacht.
 */
public class LocationDataDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = LocationDataDbHelper.class.getSimpleName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EMA_DB_Location.db";

    /** Mit diesem String wird eine Tabelle erstellt. **/
    //TODO: sind noch nicht alle Attribute
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationEntry.TABLE_NAME
            + "(" + LocationEntry.COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + LocationEntry.COLUMN_NAME + " TEXT NOT NULL, "
                  + LocationEntry.COLUMN_STREET + " TEXT, "
                  + LocationEntry.COLUMN_POSTALCODE + " TEXT, "
                  + LocationEntry.COLUMN_TOWN + " TEXT);";

    /** Konstruktor **/
    public LocationDataDbHelper(Context context) {
        //in Parameter 3 könnte man eine CursorFactory übergeben
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    /** Wird ausgeführt wenn eine DB neu angelegt wird. **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        /** An dieser Stelle eine Tabelle erzeugen **/
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit dem SQL-Befehl: " + SQL_CREATE_ENTRIES + " angelegt.");
            //db.execSQL() führt SQL-Befehle auf der Datenbank aus
            db.execSQL(SQL_CREATE_ENTRIES);
        }catch (Exception ex){
            Log.d(LOG_TAG, "Fehler beim Anlgen der Tabelle: " + ex.getMessage() );
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /** Diese innere Klasse definiert das Datenbankschema für Locations.
     * Nach '07 Datenbanken mit Android.pdf' von Steppat.**/
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        //manche klassen der Android-API erwarten diese Bezeichnung zwingend: _id
        public static final String COLUMN_ENTRY_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_POSTALCODE = "postal_code";
        public static final String COLUMN_TOWN = "town";

    }
}
