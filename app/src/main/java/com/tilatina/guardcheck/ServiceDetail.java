package com.tilatina.guardcheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


public class ServiceDetail extends AppCompatActivity {

    private SeekBar seekBar;
    private int MAX_SEEK_VALUE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        //Handle information requested by MainActivity
        Bundle intent = getIntent().getExtras();
        int id = intent.getInt("id");
        int entitiesId = intent.getInt("entitiesId");
        String name = intent.getString("name", null);
        if (null != name) {
            Log.d("JAIME...", String.format("El nombre es %s, el id del evento es %s y el número de" +
                    " servicio es %s", name, id, entitiesId));
        } else {
            Log.d("JAIME...", String.format("Nada que comentar"));
        }

        //Seek bar for status
        seekBar = (SeekBar) findViewById(R.id.changeStatusSeek);
        seekBar.setMax(MAX_SEEK_VALUE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("JAIME...", String.format("Progreso = %s", progress));

                if (progress == 0) {
                    Drawable icon = getResources().getDrawable(R.drawable.dontknow);
                    seekBar.setThumb(icon);
                }

                //The multiple of progress change is of one,
                //For put the progress in the center of the
                // field, when progress is equals to 1, the progress is set in 2
                if (progress == 1) {
                    seekBar.setProgress(2);

                //For put the progress in the end of the field, when progress is equals to 3,
                // the progress is set in 4
                } else if (progress == 3) {
                    seekBar.setProgress(4);

                }

                //TODO: Make then async task occur here for put status in the web service for "View"
                if (progress == 2) {
                    Drawable icon = getResources().getDrawable(R.drawable.view);
                    seekBar.setThumb(icon);
                }

                //TODO: Make then async task occur here for put status in the web service for "Everything OK"
                if (progress == 4) {
                    Drawable icon = getResources().getDrawable(R.drawable.ok);
                    seekBar.setThumb(icon);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        //This button make a intent to Waze to take the user to specific place.
        Button takeMePlace = (Button) findViewById(R.id.takeMePlace);
        takeMePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Change harcoded lat and long
                String uri = String.format("waze://?ll=%s, %s&navigate=yes", 19.4007304, -99.0748365);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                } catch (Exception e) {
                    new AlertDialog.Builder(ServiceDetail.this)
                            .setTitle("")
                            .setMessage("Necesitas instalar Waze para ser llevado al destino.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }

            }
        });

    }

}
