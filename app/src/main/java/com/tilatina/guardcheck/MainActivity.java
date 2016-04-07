package com.tilatina.guardcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.ServiceStatus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ServiceStatus> serviceStatusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ServiceStatusAdapter mAdapter = new ServiceStatusAdapter(serviceStatusList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE);
        String store = sharedPreferences.getString("user_id", null);
        Log.d("JAIME...", String.format("%s", store));

        recyclerView = (RecyclerView) findViewById(R.id.servicesStatusView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareServicesData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Preferences.deletePreference(getSharedPreferences(
                        Preferences.MYPREFERENCES, MODE_PRIVATE), "user_id");
                Log.d("JAIME...", "Le has picado a cerrar sesión");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareServicesData() {

        ServiceStatus serviceStatus = new ServiceStatus("Jaime", "Reportó", "5km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Brayan", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Roberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Toño", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus("Alberto", "No reportó", "50km");
        serviceStatusList.add(serviceStatus);
        mAdapter.notifyDataSetChanged();
    }
}
