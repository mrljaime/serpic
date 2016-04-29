package com.tilatina.guardcheck;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tilatina.guardcheck.Utillities.LocationGPS;
import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.WebService;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ServiceDetail extends AppCompatActivity {

    private SeekBar seekBar;
    private int MAX_SEEK_VALUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        //Handle information requested by MainActivity
        Bundle intent = getIntent().getExtras();
        final int elementId = intent.getInt("id");
        final double lat = intent.getDouble("lat");
        final double lng = intent.getDouble("lng");
        String name = intent.getString("name", null);
        String stateName = intent.getString("stateName");
        String monitorFrequency = intent.getString("monitorFrequency");
        String group = intent.getString("group");
        String stateColor = intent.getString("stateColor");
        int canModify = intent.getInt("canModify");

        if (null != name) {
        } else {
            Log.d("JAIME...", String.format("Nada que comentar"));
        }


        /**
         * Get the id of the user
         */
        final String user = Preferences
                .getPreference(getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE), Preferences.USERID, null);

        //This will change de text of the action bar for the name of the service.
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView nameService = (TextView) findViewById(R.id.nameOfService);
        nameService.setText(name);

        TextView groupText = (TextView) findViewById(R.id.groupName);
        groupText.setText("Grupo: " + group);

        TextView monitorFrequencyText = (TextView) findViewById(R.id.monitorFrequency);
        monitorFrequencyText.setText("Frecuencia de monitoreo (hrs): " + monitorFrequency);

        TextView stateText = (TextView) findViewById(R.id.stateName);
        stateText.setText(stateName);

        if ("R".equals(stateColor)) {
            stateText.setTextColor(Color.RED);
        } else if ("Y".equals(stateColor)) {
            stateText.setTextColor(Color.YELLOW);
        } else {
            stateText.setTextColor(Color.GREEN);
        }


        /**
         * Buttons declartion
         */
        Button takeMePlace = (Button) findViewById(R.id.takeMePlace);
        Button arrivalButton = (Button) findViewById(R.id.arrival);
        Button noveltyButton = (Button) findViewById(R.id.sendNovelty);
        final Button okManualButton = (Button) findViewById(R.id.okManual);
        Button changePositionButton = (Button) findViewById(R.id.changePosition);

        /**
         * If can't modify, the button okManual will be disabled
         */
        if (1 != canModify) {
            okManualButton.setEnabled(false);
        }

        //This button make a intent to Waze to take the user to specific place.
        takeMePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location location = LocationGPS.getCurrentLocation(ServiceDetail.this);
                if (null == location) {
                    return;
                }

                if (checkPackageExist(ServiceDetail.this, "com.waze")) {

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
                } else {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s",
                                    location.getLatitude(), location.getLongitude(), lat, lng
                            )));
                    startActivity(intent);
                }
            }
        });

        //This button put lat and long from the service place.
        changePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ServiceDetail.this)
                        .setTitle("")
                        .setMessage("¿Confirma que desea cambiar la ubicación del servicio a su posición actual?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Location location = LocationGPS.getCurrentLocation(ServiceDetail.this);
                                if (null == location) {
                                    return;
                                }

                                WebService.changePositionAction(ServiceDetail.this, user,
                                        String.format("%s",elementId),
                                        String.format("%s", location.getLatitude()),
                                        String.format("%s", location.getLongitude()), new WebService.ChangePositionListener() {
                                            @Override
                                            public void onSuccess(String response) {
                                                Toast.makeText(ServiceDetail.this, "Ubicación de servicio acualizada con éxito",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onError(String error) {
                                                Toast.makeText(ServiceDetail.this, "Error de comunicaciones",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
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


        okManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebService.makeOkManualAction(ServiceDetail.this, user, String.format("%s", elementId),
                        new WebService.MakeOkManualListener() {
                    @Override
                    public void onSuccess(String response) {
                        okManualButton.setEnabled(false);
                        Toast.makeText(ServiceDetail.this, "Ok manual enviado con éxito", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ServiceDetail.this, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
                    }
                });
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


    private boolean checkPackageExist(Context context, String packageName) {

        PackageManager packageManager = context.getPackageManager();

        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }
}
