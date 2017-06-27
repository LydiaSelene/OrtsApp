package com.example.lydia.mobappprojekt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;

/**
 * Created by Lydia on 14.06.2016.
 */
public class CreateALocationActivity extends AppCompatActivity {

    public final static String LOCATION_MESSAGE = "com.example.lydia.mobappprojekt.MESSAGE";

    public static final String LOG_TAG = CreateALocationActivity.class.getSimpleName();
    private LocationDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //standardtitel aus
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        Button buttonAbort = (Button) findViewById(R.id.abortButton);
        buttonAbort.setOnClickListener( new View.OnClickListener(){
            @Override
            //auf Methode leiten
            public void onClick(View v){
                CreateALocationActivity.this.onButtonAbortClick(v);
            }
        });

        Button buttonApply = (Button) findViewById(R.id.applyButton);
        buttonApply.setOnClickListener( new View.OnClickListener(){
            @Override
            //auf Methode leiten
            public void onClick(View v){
                CreateALocationActivity.this.onButtonApplyClick(v);
            }
        });

        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new LocationDataSource(this);

    }

    /** Die Activity ist sichtbar geworden und hat den Benutzer-Fokus.
     * Siehe Lifecycle-Callbacks.
     * Hier soll die Datenbankverbindung hergestellt werden.*/
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

    }

    /** Die Activity wird pausiert. Letzter mit Garantie aufgerufener
     * Callback, bevor die Activity zerstört werden kann. Siehe Lifecycle-Callbacks.
     * Beispielsweise tritt anschließend eine andere Activity in den Vordergrund, die die vorherige
     * aber nicht komplett verdeckt, z.B. ein Dialog. Diese Activity hier ist dann pausiert (aber nicht zerstört, außer bei Speichermangel)
     * (Bei vollständiger Verdeckung würde sie gestoppt, aber intakt bleiben).
     * Hier sollen alle Zustände und Änderungen in der Datenbank gespeichert werden. Die Datenbankverbindung muss dann getrennt werden.*/
    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }



    private void onButtonAbortClick(View v) {
        //finish() aufrufen, wenn alles erledigt ist und die activity schließen soll
        //onDestroy wird ausgeführt
        //ohne System.exit() wird die Activity bzw. App nur minimiert und änderungen werden zurückgesetzt
        finish();
    }

    private void onButtonApplyClick(View v) {

        EditText nameField = (EditText) findViewById(R.id.edit_location_name);
        EditText streetField = (EditText) findViewById(R.id.edit_location_street);
        EditText postalCodeField = (EditText) findViewById(R.id.edit_location_postalCode);
        EditText townField = (EditText) findViewById(R.id.edit_location_town);

        //TODO: Behandlung: Name darf nicht leer sein! Und dem Benutzer melden.
        if( TextUtils.isEmpty(nameField.getText()) ) {
            nameField.setHint("Name fehlt!");
            nameField.setHintTextColor( ContextCompat.getColor(this, R.color.colorHintAlert) );
            //TODO: bessere Fehlermeldungen
            //nameField.setError(getString(R.string.edit_location_name));

        }else {
            /*
            Intent resultIntent = new Intent();
            resultIntent.putExtra(LOCATION_MESSAGE, nameField.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
            */
            /** Hier die Daten als neues Objekt in die Datenbank schreiben */
            String nameValue = nameField.getText().toString();
            String streetValue = streetField.getText().toString();
            String postalCodeValue = postalCodeField.getText().toString();
            String townValue = townField.getText().toString();


            dataSource.createLocationData(nameValue, streetValue, postalCodeValue, townValue);

            finish();
        }
    }

} //class
