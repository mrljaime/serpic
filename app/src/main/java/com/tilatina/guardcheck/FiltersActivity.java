package com.tilatina.guardcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        String defaultStatus = "Todos";
        final List<String> status = new ArrayList<>();
        status.add("Todos");
        status.add("Rojo");
        status.add("Amarillo");
        status.add("Verde");

        List<String> sortBy = new ArrayList<>();
        sortBy.add("Nombre");
        sortBy.add("Estado/Fecha");
        sortBy.add("Distancia");

        final List<String> sort = new ArrayList<>();
        sort.add("Ascendente");
        sort.add("Descendente");

        final Spinner statusSpin = (Spinner) findViewById(R.id.status_filter);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, status);
        statusSpin.setAdapter(statusAdapter);
        statusSpin.setSelection(statusAdapter.getPosition(defaultStatus));

        final Spinner sortBySpin = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, sortBy);
        sortBySpin.setAdapter(sortByAdapter);

        final Spinner sortSpin = (Spinner) findViewById(R.id.sort);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, sort);
        sortSpin.setAdapter(sortAdapter);

        Button applyFilters = (Button) findViewById(R.id.applyFilters);
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = statusSpin.getSelectedItem().toString();
                String sortBy = sortBySpin.getSelectedItem().toString();
                String sort = sortSpin.getSelectedItem().toString();
                Intent intent = new Intent();
                Log.d("JAIME...", "Antes de poner extras a intent para retroceder");
                intent.putExtra("filter", filter);
                intent.putExtra("sortBy", sortBy);
                intent.putExtra("sort", sort);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
