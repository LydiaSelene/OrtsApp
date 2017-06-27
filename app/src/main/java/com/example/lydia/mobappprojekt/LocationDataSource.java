package com.example.lydia.mobappprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lydia on 25.06.2016.
 *Diese Klasse ist für alle Datenbankzugriffe verantwortlich.
 */
public class LocationDataSource {

    private static final String LOG_TAG = LocationDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private LocationDataDbHelper dbHelper;
    private String[]    columns = {
                            LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID,
                            LocationDataDbHelper.LocationEntry.COLUMN_NAME,
                            LocationDataDbHelper.LocationEntry.COLUMN_STREET,
                            LocationDataDbHelper.LocationEntry.COLUMN_POSTALCODE,
                            LocationDataDbHelper.LocationEntry.COLUMN_TOWN
                        };

    /** Konstruktor */
    public LocationDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new LocationDataDbHelper(context);
    }

    /** DB_Verbindung öffnen **/
    public void open(){
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wir abgefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "DB-Referenz erhalten. Pfad zur Datenbank: "+database.getPath() );
    }

    public void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DBHelpers geschlossen.");
    }

    /** Datensätze in die Tabelle der Datenbank einfügen */
    public LocationData createLocationData(String name, String street, String postalCode, String town){
        /** in die DB einzutragenden Inhalte, Key-Value-Pairs **/
        ContentValues values = new ContentValues();
        /** Spaltenname der Tabelle, Wert */
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_NAME, name);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_STREET, street);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_POSTALCODE, postalCode);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_TOWN, town);

        /** Datensatz in Tabelle der DB einfügen */
        long insertId = database.insert(LocationDataDbHelper.LocationEntry.TABLE_NAME, null, values);

        //nach einem Datensatz mit bestimmter ID suchen
        /** Der Cursor ist ein Datenzugriffsobjekt mit dem auf die Daten des Suchergebnisses zugegriffen
         * werden kann. **/
        Cursor cursor = database.query( LocationDataDbHelper.LocationEntry.TABLE_NAME,
                                        columns,
                                        LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID + "=" + insertId,
                                        null, null, null, null);
        cursor.moveToFirst();
        LocationData locData = cursorToLocationData(cursor);
        cursor.close();

        return locData;
    }

    /** Datensätze in der Tabelle der Datenbank ändern */
    public LocationData updateLocationData(String name, String street, String postalCode, String town, long id){
        /** in die DB einzutragenden Inhalte, Key-Value-Pairs **/
        ContentValues values = new ContentValues();
        /** Spaltenname der Tabelle, Wert */
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_NAME, name);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_STREET, street);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_POSTALCODE, postalCode);
        values.put(LocationDataDbHelper.LocationEntry.COLUMN_TOWN, town);

        database.update(LocationDataDbHelper.LocationEntry.TABLE_NAME,
                        values,
                        LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID + "=" + id,
                        null);

        /** Der Cursor ist ein Datenzugriffsobjekt mit dem auf die Daten des Suchergebnisses zugegriffen
         * werden kann. **/
        Cursor cursor = database.query( LocationDataDbHelper.LocationEntry.TABLE_NAME,
                                        columns,
                                        LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID + "=" + id,
                                        null, null, null, null);

        cursor.moveToFirst();
        LocationData locData = cursorToLocationData(cursor);
        cursor.close();

        return locData;
    }

    /** Löscht Orts-Einträge aus der Datenbank */
    public void deleteLocationData(LocationData location){
        long id = location.getId();

        database.delete(LocationDataDbHelper.LocationEntry.TABLE_NAME,
                        LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID + "=" +id,
                        null);

        Log.d(LOG_TAG, "Eintrag gelöscht! ID: "+id+" , Inhalt: "+ location.toString());
    }

    /** Wandelt ein Cursor-Objekt in ein LocationData-Objekt um. */
    private LocationData cursorToLocationData(Cursor cursor){
        /** Indizes der Tabellenspalten */
        int idIndex = cursor.getColumnIndex(LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID);
        int idName = cursor.getColumnIndex(LocationDataDbHelper.LocationEntry.COLUMN_NAME);
        int idStreet = cursor.getColumnIndex(LocationDataDbHelper.LocationEntry.COLUMN_STREET);
        int idPostalCode = cursor.getColumnIndex(LocationDataDbHelper.LocationEntry.COLUMN_POSTALCODE);
        int idTown = cursor.getColumnIndex(LocationDataDbHelper.LocationEntry.COLUMN_TOWN);

        String name = cursor.getString(idName);
        String street = cursor.getString(idStreet);
        String postalCode = cursor.getString(idPostalCode);
        String town = cursor.getString(idTown);
        long id = cursor.getLong(idIndex);

        LocationData locData = new LocationData(name, street, postalCode, town, id);

        return locData;
    }

    /** Alle Datensätze aus der Tabelle der Datenbank holen. */
    public List<LocationData> getAllLocationData(){
        List<LocationData> locationDataList = new ArrayList<>();

        Cursor cursor = database.query( LocationDataDbHelper.LocationEntry.TABLE_NAME,
                                        columns,
                                        null, null, null, null, null);

        cursor.moveToFirst();
        LocationData locationData;

        while(!cursor.isAfterLast()){
            locationData = cursorToLocationData(cursor);
            locationDataList.add(locationData);
            Log.d(LOG_TAG, "ID: "+ locationData.getId() + ", Inhalt: " + locationData.toString());
            cursor.moveToNext();
        }

        cursor.close();
        return locationDataList;
    }

    /** Einen Datensatz aus der Tabelle der Datenbank holen. */
    public LocationData getOneLocationData(long id){

        Log.d(LOG_TAG, "starte db-query für cursor");

        Cursor cursor = database.query( LocationDataDbHelper.LocationEntry.TABLE_NAME,
                                        columns,
                                        LocationDataDbHelper.LocationEntry.COLUMN_ENTRY_ID + "=" + id,
                                        null, null, null, null);


        Log.d(LOG_TAG, "db-query klappte, jetzt cursor-action");

        cursor.moveToFirst();
        LocationData locData = cursorToLocationData(cursor);
        cursor.close();

        return locData;
    }

} //class
