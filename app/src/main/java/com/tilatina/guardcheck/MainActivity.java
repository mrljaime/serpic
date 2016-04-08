package com.tilatina.guardcheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.ServiceStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ServiceStatus serviceStatus = serviceStatusList.get(position);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ServiceDetail.class);
                intent.putExtra("name", serviceStatus.getName());
                intent.putExtra("statusDate", serviceStatus.getStatusDate());
                intent.putExtra("id", serviceStatus.getId());
                intent.putExtra("entitiesId", serviceStatus.getEntitiesId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
                return false;
            case R.id.filters:
                Toast.makeText(getApplicationContext(), "Filtros", Toast.LENGTH_SHORT).show();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final
            MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                   return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void prepareServicesData() {

        ServiceStatus serviceStatus = new ServiceStatus(1, 2, "Jaime", "Reportó", "5km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        serviceStatus = new ServiceStatus(1, 2, "Daniel", "Reportó", "50km");
        serviceStatusList.add(serviceStatus);
        mAdapter.notifyDataSetChanged();
    }
}
