package com.tilatina.guardcheck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.tilatina.guardcheck.Utillities.LocationGPS;
import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.ServiceStatus;
import com.tilatina.guardcheck.Utillities.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ServiceStatus> serviceStatusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ServiceStatusAdapter mAdapter = new ServiceStatusAdapter(serviceStatusList);
    SwipeRefreshLayout swipeToRefresh;
    ProgressDialog progresDialog;
    Context me = this;

    String status = "";
    String orderBy = "";
    String sort = "";
    String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE);
        String store = sharedPreferences.getString("user_id", null);
        Log.d("JAIME...", String.format("%s", store));

        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
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
                intent.putExtra("id", serviceStatus.getId());
                intent.putExtra("lat", serviceStatus.getLat());
                intent.putExtra("lng", serviceStatus.getLng());
                intent.putExtra("canModify", serviceStatus.getCanModify());
                intent.putExtra("stateName", serviceStatus.getStateName());
                intent.putExtra("group", serviceStatus.getGroup());
                intent.putExtra("monitorFrequency", serviceStatus.getMonitorFrequency());
                intent.putExtra("stateColor", serviceStatus.getstatusColor());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareServicesData(me);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent, R.color.greenForActions);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareServicesData(me);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.logout:
                Preferences.deletePreference(getSharedPreferences(
                        Preferences.MYPREFERENCES, MODE_PRIVATE), "user_id");
                Log.d("JAIME...", "Le has picado a cerrar sesión");
                intent.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            case R.id.filters:
                intent.setClass(getApplicationContext(), FiltersActivity.class);
                startActivityForResult(intent, 1);
                return false;
            case R.id.search:
                dialogForSearch(me);
                return false;
            case R.id.refresh:
                prepareServicesData(me);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.d(Preferences.MYPREFERENCES, "On resume");
        //prepareServicesData(me);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("JAIME...", "En onActivityResult");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                transformFilters(data);
                prepareServicesData(me);

            }
        }
    }

    //Some things to make useful the recycler view

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

    private void prepareServicesData(Context context){
        final Context mContext = context;
        progresDialog = new ProgressDialog(context);
        progresDialog.setMessage("Cargando");
        progresDialog.show();

        serviceStatusList.clear();
        mAdapter.notifyDataSetChanged();
        Location location = LocationGPS.getCurrentLocation(context);
        if (null == location) {
            Toast.makeText(context, "No se ha podido tomar posición", Toast.LENGTH_SHORT).show();
            progresDialog.hide();
            swipeToRefresh.setRefreshing(false);

            return;
        }

        String user = Preferences
                .getPreference(context.getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE),
                        Preferences.USERID, null);
        WebService.getServicesAction(context, user, status, orderBy, sort, search,
                String.format("%s", location.getLatitude()), String.format("%s", location.getLongitude()),
                new WebService.ServicesSuccessListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray services = jsonResponse.getJSONArray("elements");

                            for (int i = 0; i < services.length(); i++) {
                                Log.d(Preferences.MYPREFERENCES, services.getJSONObject(i).toString());
                                ServiceStatus serviceStatus = new ServiceStatus();
                                serviceStatus.setId(services.getJSONObject(i).getInt("id"));
                                serviceStatus.setName(services.getJSONObject(i).getString("name"));
                                serviceStatus.setNextTo(services.getJSONObject(i).getString("distance"));

                                if (!JSONObject.NULL.equals(services.getJSONObject(i).get("lat"))) {
                                    serviceStatus.setLat(services.getJSONObject(i).getDouble("lat"));
                                } else {
                                    serviceStatus.setLat(0);
                                }
                                if (!JSONObject.NULL.equals(services.getJSONObject(i).get("lng"))) {
                                    serviceStatus.setLng(services.getJSONObject(i).getDouble("lng"));
                                } else {
                                    serviceStatus.setLng(0);
                                }

                                serviceStatus.setstatusColor(services.getJSONObject(i).getString("statusColor"));
                                serviceStatus.setCanModify(services.getJSONObject(i).getInt("canModify"));
                                serviceStatus.setStateName(services.getJSONObject(i).getString("stateName"));
                                serviceStatus.setMonitorFrequency(services.getJSONObject(i).getString("monitorFrequency"));
                                serviceStatus.setGroup(services.getJSONObject(i).getString("groupName"));

                                serviceStatusList.add(serviceStatus);
                            }

                            progresDialog.hide();
                            mAdapter.notifyDataSetChanged();
                            swipeToRefresh.setRefreshing(false);
                            cleanFiltersStrings();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progresDialog.hide();
                            mAdapter.notifyDataSetChanged();
                            swipeToRefresh.setRefreshing(false);
                            cleanFiltersStrings();
                        }

                    }
                }, new WebService.ServicesErrorListener() {
                    @Override
                    public void onError(String error) {
                        progresDialog.hide();
                        swipeToRefresh.setRefreshing(false);
                        Toast.makeText(mContext, "Error de comunicaciones", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }

    private void transformFilters(Intent data) {

        if (data.getStringExtra("sortBy").equals("Nombre y distancia")) {
            orderBy = "1";
        } else if (data.getStringExtra("sortBy").equals("Estado y distancia")) {
            orderBy = "2";
        } else if (data.getStringExtra("sortBy").equals("Distancia")) {
            orderBy = "3";
        }

        if (data.getStringExtra("sort").equals("Ascendente")) {
            sort = "true";
        } else {
            sort = "false";
        }
    }

    private void cleanFiltersStrings(){
        status = "";
        orderBy = "";
        sort = "";
        search = "";
    }

    private void dialogForSearch(Context context) {
        final Context mContext = context;
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(mContext);
        alerBuilder.setMessage("Búsqueda");
        alerBuilder.setView(R.layout.search);
        alerBuilder.setPositiveButton("Búscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = alerBuilder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) dialog.findViewById(R.id.searchInput);
                search = searchText.getText().toString().trim();
                prepareServicesData(mContext);
                dialog.dismiss();
            }
        });
    }

    private boolean diffNull(String value) {
        if (null == value) {
            return false;
        }

        return true;
    }
}
