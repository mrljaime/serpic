package com.tilatina.guardcheck;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SpinnerAdapter;


public class ServiceDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        //With this one we can go back in interface


        Bundle intent = getIntent().getExtras();
        String name = intent.getString("name", null);
        if (null != name) {
            Log.d("JAIME...", String.format("El nombre es %s", name));
        } else {
            Log.d("JAIME...", String.format("Nada que comentar"));
        }

    }

}
