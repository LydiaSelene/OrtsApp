package com.example.lydia.mobappprojekt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        Button buttonMainLocations = (Button) findViewById(R.id.buttonMainLocations);
        buttonMainLocations.setOnClickListener( new View.OnClickListener(){
            @Override
            //auf Methode leiten
            public void onClick(View v){
                MainActivity.this.onButtonMainLocationsClick(v);
            }
        });

        Button buttonMainDJWish = (Button) findViewById(R.id.buttonMainDJWish);
        buttonMainDJWish.setOnClickListener( new View.OnClickListener(){
                    @Override
                    //auf Methode leiten
                    public void onClick(View v){
                        MainActivity.this.onButtonMainDJWishClick(v);
                    }
                });

        Button buttonMainTasks = (Button) findViewById(R.id.buttonMainTasks);
        buttonMainTasks.setOnClickListener( new View.OnClickListener(){
            @Override
            //auf Methode leiten
            public void onClick(View v){
                MainActivity.this.onButtonMainTasksClick(v);
            }
        });

        Button buttonMainExit = (Button) findViewById(R.id.buttonMainExit);
        buttonMainExit.setOnClickListener( new View.OnClickListener(){
            @Override
            //auf Methode leiten
            public void onClick(View v){
                MainActivity.this.onButtonMainExitClick(v);
            }
        });
    }

    private void onButtonMainLocationsClick(View view) {
        //die Activity ist eine unterklasse vom Context (this)
        //der Intent soll zur Class gehen (activity starten)
        Intent intent = new Intent(this, LocationListActivity.class);
        startActivity(intent);

    }

    private void onButtonMainDJWishClick(View view) {
        Button b = (Button) view.findViewById(R.id.buttonMainDJWish);
        if(!b.getText().toString().endsWith("gedrückt")){
            b.setText( b.getText()+"\ngedrückt");
        }
    }

    private void onButtonMainTasksClick(View view) {
        Button b = (Button) view.findViewById(R.id.buttonMainTasks);
        if(!b.getText().toString().endsWith("gedrückt")){
            b.setText( b.getText()+"\ngedrückt");
        }
    }

    private void onButtonMainExitClick(View view) {
        Button b = (Button) view.findViewById(R.id.buttonMainExit);
        if(!b.getText().toString().endsWith("gedrückt")){
            b.setText( b.getText()+"\ngedrückt");
        }
        //finish() aufrufen, wenn alles erledigt ist und die activity schließen soll
        //onDestroy wird ausgeführt
        //ohne System.exit() wird die Activity bzw. App nur minimiert und änderungen werden zurückgesetzt
        finish();
        //die app wird nur minimiert !? bzw. liegt immernoch im ram !?
        System.exit(0);
    }

    /** Menu icons are inflated just as they were with actionbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_centered_title, menu);
        return true;
    }

}
