package com.tilatina.guardcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tilatina.guardcheck.Utillities.Preferences;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button accept = (Button) findViewById(R.id.loginButton);
        final AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        final SharedPreferences sharedPreferences =
                getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE);
        String store = sharedPreferences.getString("user_id", null);
        Log.d("JAIME...", String.format("%s", store));

        if (null != store) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldLenghts(username.getText().toString(), password.getText().toString())) {
                    Preferences.putPreference(sharedPreferences, "user_id", "1");
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    intent.putExtra("isActive", 1);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Necesitas llenar ambos campos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean checkFieldLenghts(String username, String password) {

        if ( 0 == username.length() || 0 == password.length() ) {
            Log.d("JAIME...", "La validación de campos no ha pasado");
            return false;
        }
        Log.d("JAIME...", "La validación de campos no ha pasado");
        return true;
    }
}
