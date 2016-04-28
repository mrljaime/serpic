package com.tilatina.guardcheck;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


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
        final double lat = intent.getDouble("lat");
        final double lng = intent.getDouble("lng");

        String name = intent.getString("name", null);
        if (null != name) {
            Log.d("JAIME...", String.format("El nombre es %s, el id del evento es %s y el número de" +
                    " servicio es %s", name, id, entitiesId));
        } else {
            Log.d("JAIME...", String.format("Nada que comentar"));
        }

        //This will change de text of the action bar for the name of the service.
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                // field, when progress is equals to 1, the progress is seted in 2
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
                String uri = String.format("waze://?ll=%s, %s&navigate=yes", lat, lng);
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

        //This button put lat and long from the service place.
        Button isHere = (Button) findViewById(R.id.isHere);
        isHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ServiceDetail.this)
                        .setTitle("")
                        .setMessage("¿Confirma cambiar la ubicación del servicio?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double[] latLng = getLatLong();
                                if (null != latLng) {
                                    Toast.makeText(getApplicationContext(),
                                            String.format("Latitud = %s, Longitud = %s", latLng[0], latLng[1]),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //This method will change, but for now, is for get the lat and the long of the device.
    private double[] getLatLong() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location;
        double [] latLng = null;
        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("JAIME...", "El problema esta en que no hay gps encendido");
                printLocationError("No se ha podido obtener localización, por favor encienda sus datos y/o " +
                        "gps para poder obtenerla");
                return latLng;
            }

            if (null != locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)) {
                Log.d("JAIME...", "Debería dar posición por la comprobación GPS");
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latLng = new double[] {location.getLatitude(), location.getLongitude()};

                return latLng;
            }

            if (null != locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)) {
                Log.d("JAIME...", "Debería dar posición por la comprobación NETWORK");
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latLng = new double[] {location.getLatitude(), location.getLongitude()};

                return latLng;
            }
        }catch (SecurityException e) {
            printLocationError("Debes aceptar los permisos para poder utilizar la geolocalización");
        }

        return latLng;
    }

    //This method is called for display error message when GPS is disabled
    private void printLocationError(String message) {
        new AlertDialog.Builder(ServiceDetail.this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
