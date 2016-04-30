package com.tilatina.guardcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        final RadioGroup groupOrderBy = (RadioGroup) findViewById(R.id.orderBy);
        final RadioGroup sort = (RadioGroup) findViewById(R.id.sort);

        Button applyFilters = (Button) findViewById(R.id.applyFilters);
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int orderById = groupOrderBy.getCheckedRadioButtonId();
                RadioButton orderByButton = (RadioButton) findViewById(orderById);

                int sortId = sort.getCheckedRadioButtonId();
                RadioButton sortButton = (RadioButton) findViewById(sortId);

                String sortBy = orderByButton.getText().toString();
                String sort = sortButton.getText().toString();
                Intent intent = new Intent();
                Log.d("JAIME...", "Antes de poner extras a intent para retroceder");
                intent.putExtra("sortBy", sortBy);
                intent.putExtra("sort", sort);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
