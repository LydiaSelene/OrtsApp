package com.example.lydia.mobappprojekt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import android.view.inputmethod.InputMethodManager;
import android.util.SparseBooleanArray;
import android.support.v7.view.ActionMode;
//import android.view.ActionMode;


public class LocationListActivity extends AppCompatActivity {

    public static final String LOG_TAG = LocationListActivity.class.getSimpleName();
    private LocationDataSource dataSource;

    // a request code
    static final int CREATE_LOCATION_REQUEST = 1;


    /** Die Activity wird erstellt. Siehe Lifecycle-Callbacks. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationlist);

        /** Find the toolbar view inside the activity layout */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /** Sets the Toolbar to act as the ActionBar for this Activity window.
         Make sure the toolbar exists in the activity and is not null */
        setSupportActionBar(toolbar);
        /** backbutton einstellen */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** icon setzen für toolbar-navigation */
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);


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

        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden: ");
        initializeLocationList();
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

    /** Alle Objekte in der Liste in der View anzeigen **/
    private void initializeLocationList(){
        ArrayList<LocationData> locationDataList = (ArrayList<LocationData>) dataSource.getAllLocationData();

        final ListView locationDataListView = (ListView) findViewById(R.id.listView_locations);
        locationDataListView.setAdapter(new LocationListAdapter(this, locationDataList));
        /** Elemente der Liste klickbar machen.
         * Ein Klick soll die eigene Ortsansicht öffnen. */
        locationDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                LocationData location = (LocationData) locationDataListView.getItemAtPosition(position);
                Intent intent = new Intent(LocationListActivity.this, LocationViewActivity.class);
                intent.putExtra("locationDataBase_Id", location.getId());
                startActivity(intent);
            }
        });

        /** Wenn Elemente lang angeklickt werden */
        initializeContextualActionBar();
    }

    /**Das Plus in der ToolBar wurde gedrückt. Nun einen neuen Ort anlegen.**/
    public void onToolBar_Add (MenuItem mi) {

        /**die Activity ist eine unterklasse vom Context (this)
        der Intent soll zur Class gehen (activity starten)**/

        Intent i = new Intent(this, CreateALocationActivity.class);
        startActivityForResult(i, CREATE_LOCATION_REQUEST);
    }

    /** testfunktion zum speichern in DB **/
    public void onToolBar_DBtest (MenuItem mi) {

        dataSource.createLocationData("name", "straße", "postalCode", "town");

        /**das eingabefeld (software keyboard) verschwinden lassen (wenn woanders hingeklickt) für die sicht auf die komplette liste
         * von http://www.programmierenlernenhq.de/mit-sqlite-app-auf-benutzereingaben-reagieren-in-android/ */
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //aktualisiert alle einträge der DB zeigen
        initializeLocationList();
    }

    /** Wenn Elemente lang angeklickt werden */
    private void initializeContextualActionBar(){

        final ListView locationsListView = (ListView) findViewById(R.id.listView_locations);
        /** dadurch können mehrere Listeneinträge gewählt werden */
        locationsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        /** MultiChoiceModeListener implementieren, für das ListView-Obj registrieren, */
        //locationsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
        locationsListView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener(){

            /** Auf das Auswählen von Einträgen reagieren und den Text in der CAB anpassen,
                von hier kann ein mode.invalidate() angefordert werden. */
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked){
                final ListView locationsListView = (ListView) findViewById(R.id.listView_locations);
                int countSelected = locationsListView.getCheckedItemCount();
                String selectedText;
                //TODO: besonders für mehrere Sprachen statische Strings auslagern!
                if(countSelected < 2){
                    selectedText = " Ort gewählt";
                }else{
                    selectedText = " Orte gewählt";
                }
                mode.setTitle(countSelected + selectedText);

                LocationData item = (LocationData) locationsListView.getItemAtPosition(position);

            }

            /** Das Menü mit CAB-Actions füllen */
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu){
                // erstelltes CAB-Menü nutzen /
                getMenuInflater().inflate(R.menu.menu_locations_contextual_action_bar, menu);
                /**workaround für das cab <-> toolbar supportActionBar problem */
                //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                //toolbar.setVisibility(View.GONE);
                return true;
            }

            /** Updates an der CAb vornehmen, indem man auf invalidate()-Anfragen reagiert. */
            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu){
                return false;
            }

            /** Auf Klicks der CAB-Actions reagieren.
                Welche Action wurde angeklickt ? -> handeln */
            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item){
                switch(item.getItemId()){
                    case R.id.cab_delete:
                        SparseBooleanArray touchedLocationPositions = locationsListView.getCheckedItemPositions();
                        for(int i=0; i < touchedLocationPositions.size(); i++){
                            boolean isChecked = touchedLocationPositions.valueAt(i);
                            if(isChecked){
                                int positionInListView = touchedLocationPositions.keyAt(i);
                                LocationData location = (LocationData) locationsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "Position in ListView: "+ positionInListView +" Inhalt: "+ location.toString());
                                dataSource.deleteLocationData(location);
                            }
                        }
                        initializeLocationList();
                        // CAB schließen /
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            /** Aktualisierungen an der Activity vornehmen, wenn die CAB entfernt wird.
                Standardmäßig werden die gewählten Einträge wieder freigegeben.
                @param mode */
            @Override
            public void onDestroyActionMode(android.view.ActionMode mode){

            }
        }); //locationsListView.setMultiChoiceModeListener(
    } //initializeContextualActionBar


    /** Reagiert auf einen neu erstellten Ort. veraltet. **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        switch(requestCode) {
            case (CREATE_LOCATION_REQUEST) : {
                if (resultCode == RESULT_OK) {
                    String locationTitle = data.getStringExtra(CreateALocationActivity.LOCATION_MESSAGE);
                    // TODO Update your TextView.

                    Button button = new Button(this);
                    button = (Button) getLayoutInflater().inflate(R.layout.location_button, null);
                    button.setText(locationTitle);
                    button.setTextSize(24);
                    LinearLayout layout = (LinearLayout) findViewById(R.id.locationContent);
                    layout.addView(button);
                }
                break;
            }
        } */
    }

    /** Menu icons are inflated just as they were with actionbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locations, menu);
        return true;
    }

}
