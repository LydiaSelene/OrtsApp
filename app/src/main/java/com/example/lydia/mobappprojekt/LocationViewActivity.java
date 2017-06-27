package com.example.lydia.mobappprojekt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Lydia on 17.07.2016.
 * Die Activity für die Ansicht eines angelegten Ortes.
 */
public class LocationViewActivity extends AppCompatActivity {

    public static final String LOG_TAG = LocationViewActivity.class.getSimpleName();
    private LocationDataSource dataSource;
    private long location_Id;
    private LocationData thisLocation;
    private boolean isLocationChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /** backbutton einstellen */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** icon setzen für toolbar-navigation */
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);

        long location_Id = getIntent().getExtras().getLong("locationDataBase_Id");
        this.location_Id = location_Id;

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

        loadLocationData(location_Id);
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

        /** Ort in Datenbank updaten, wenn er geändert wurde. */
        if(isLocationChanged){
            updateLocationInDatabase();
            /**falls die app z.b. minimiert wird, oder ein dialog kommt und es dann in die activity zurückgeht -
             * so muss nicht mehrmahls ohne änderungen gespeichert werden */
            isLocationChanged = false;
        }

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    protected void loadLocationData(long id) {
        Log.d(LOG_TAG, "versuche jetzt loc zu holen");
        LocationData locationData = dataSource.getOneLocationData(id);
        thisLocation = locationData;

        Log.d(LOG_TAG, "loc holen hat geklappt");

        TextView textView_title = (TextView) findViewById(R.id.textView_Title);
        /** Der Name ist immer vorhanden */
        textView_title.setText(locationData.getName());

        TextView textView_street_postal_town = (TextView) findViewById(R.id.textView_street_postal_town);
        String street = "[Straße]";
        String postal = "[PLZ]";
        String town = "[Ort]";
        //TODO: auch prüfen, ob es nicht nur Leerzeichen sind
        if(!locationData.getStreet().isEmpty()){
            street = locationData.getStreet();
        }
        if(!locationData.getPostalCode().isEmpty()){
            postal = locationData.getPostalCode();
        }
        if(!locationData.getTown().isEmpty()){
            town = locationData.getTown();
        }
        textView_street_postal_town.setText(street + ",\n" + postal + ", " + town);
    }

    /**Den Namen des Ortes editieren. **/
    public void on_editTitle(View view) {
        final TextView tview = (TextView) findViewById(R.id.textView_Title);
        final EditText tedit = (EditText) findViewById(R.id.editText_Title);

        tview.setVisibility(View.GONE);
        tedit.setText(tview.getText());
        tedit.setVisibility(View.VISIBLE);
        tedit.requestFocus();

        /**für das erscheinen der soft tastatur bei fokuswechsel*/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tedit, InputMethodManager.SHOW_IMPLICIT);

        /** wenn in tastatur "ok", dann editText weg und wieder textview zeigen, werte übertragen */
        tedit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /** wenn der text verändert wurde */
                    if(!thisLocation.getName().equals( tedit.getText().toString() )){
                        thisLocation.setName( tedit.getText().toString() );
                        tview.setText( tedit.getText() );
                        isLocationChanged = true;
                    }
                    tedit.setVisibility(View.GONE);
                    tview.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tedit.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    } //on_editTitle

    /**Die klassische Adresse des Ortes editieren. (Straße, PLZ, Ort) **/
    public void on_editAdressAttributes(View view) {
        final TextView street_postal_townView = (TextView) findViewById(R.id.textView_street_postal_town);
        street_postal_townView.setVisibility(View.GONE);

        final EditText streetEdit = (EditText) findViewById(R.id.editText_Street);
        streetEdit.setText(thisLocation.getStreet());
        streetEdit.setVisibility(View.VISIBLE);
        streetEdit.requestFocus();

        final EditText postalEdit = (EditText) findViewById(R.id.editText_Postal);
        postalEdit.setText(thisLocation.getPostalCode());
        postalEdit.setVisibility(View.VISIBLE);

        final EditText townEdit = (EditText) findViewById(R.id.editText_Town);
        townEdit.setText(thisLocation.getTown());
        townEdit.setVisibility(View.VISIBLE);

        /**für das erscheinen der soft tastatur bei fokuswechsel*/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(streetEdit, InputMethodManager.SHOW_IMPLICIT);

        /** wenn in tastatur "ok", dann editText weg und wieder textview zeigen, werte übertragen */
        streetEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /** wenn der text verändert wurde */
                    handleAdressEditClosing(streetEdit);
                    return true;
                }
                return false;
            }
        });

        /** wenn in tastatur "ok", dann editText weg und wieder textview zeigen, werte übertragen */
        postalEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /** wenn der text verändert wurde */
                    handleAdressEditClosing(postalEdit);
                    return true;
                }
                return false;
            }
        });

        /** wenn in tastatur "ok", dann editText weg und wieder textview zeigen, werte übertragen */
        townEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /** wenn der text verändert wurde */
                    handleAdressEditClosing(townEdit);
                    return true;
                }
                return false;
            }
        });
    } //on_editAdressAttributes

    /** Prüfen, ob und was bei Straße, PLZ und Ort geändert wurde - wenn was gedändert wurde, thisLocation aktualisieren und
     * TextViews aktualisieren.*/
    void handleAdressEditClosing(EditText edit){
        final TextView street_postal_townView = (TextView) findViewById(R.id.textView_street_postal_town);

        final EditText streetEdit = (EditText) findViewById(R.id.editText_Street);
        final EditText postalEdit = (EditText) findViewById(R.id.editText_Postal);
        final EditText townEdit = (EditText) findViewById(R.id.editText_Town);

        if(!thisLocation.getStreet().equals( streetEdit.getText().toString() )){
            thisLocation.setStreet( streetEdit.getText().toString() );
            isLocationChanged = true;
        }

        if(!thisLocation.getPostalCode().equals( postalEdit.getText().toString() )){
            thisLocation.setPostalCode( postalEdit.getText().toString() );
            isLocationChanged = true;
        }

        if(!thisLocation.getTown().equals( townEdit.getText().toString() )){
            thisLocation.setTown( townEdit.getText().toString() );
            isLocationChanged = true;
        }

        if(isLocationChanged){
            street_postal_townView.setText(thisLocation.getStreet() + ",\n" + thisLocation.getPostalCode() + ", " + thisLocation.getTown());
        }

        streetEdit.setVisibility(View.GONE);
        postalEdit.setVisibility(View.GONE);
        townEdit.setVisibility(View.GONE);
        street_postal_townView.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

    } //checkForChangedAdressAttributes

    public void updateLocationInDatabase() {
        dataSource.updateLocationData(
                thisLocation.getName(),
                thisLocation.getStreet(),
                thisLocation.getPostalCode(),
                thisLocation.getTown(),
                thisLocation.getId()
        );

    }

}
