package com.tilatina.guardcheck;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


public class ServiceDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Button takeMePlace = (Button) findViewById(R.id.takeMePlace);
        takeMePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

}
