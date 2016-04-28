package com.tilatina.guardcheck;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tilatina.guardcheck.Dao.User;
import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Context me = LoginActivity.this;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Declartions of ui
         */
        Button accept = (Button) findViewById(R.id.loginButton);
        final AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        /**
         * Searching the id from user logged, if exists, start main activity else continue.
         */
        final SharedPreferences sharedPreferences =
                getSharedPreferences(Preferences.MYPREFERENCES, MODE_PRIVATE);
        String store = sharedPreferences.getString(Preferences.USERID, null);
        Log.d(Preferences.MYPREFERENCES, String.format("%s", store));

        /**
         * Searching id
         */
        if (null != store) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(me);
        alertDialog.setMessage("Teclee su nombre de dominio")
                .setView(R.layout.domain_view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean close = false;
                EditText domainEditText = (EditText) dialog.findViewById(R.id.domainInput);
                if (domainEditText.getText().toString().trim().length() != 0) {
                    domain = domainEditText.getText().toString().trim();
                    close = true;
                } else {
                    domainEditText.setError("El campo es obligatorio");
                }
                if (close) {
                    dialog.dismiss();
                }
            }
        });
        /**
         * Login button listener
         */
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldLenghts(username.getText().toString(), password.getText().toString())) {

                    setEnabledInput(username, password, false);
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Cargando");
                    progressDialog.show();

                    WebService loginAction = new WebService();
                    loginAction.loginAction(username.getText().toString(), password.getText().toString(),
                            domain,
                            getApplicationContext(), new WebService.LoginSuccessListener() {
                                @Override
                                public void onSuccess(String response) {
                                    progressDialog.dismiss();

                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        if (200 == jsonResponse.getInt("code")) {
                                            Preferences.putPreference(sharedPreferences, Preferences.USERID,
                                                    jsonResponse.getString("id"));
                                            //Start the main activity
                                            Intent intent = new Intent();
                                            intent.setClass(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(me, "Nombre de usuario o contraseña incorrectos",
                                                    Toast.LENGTH_LONG).show();
                                            dialog.show();
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new WebService.LoginErrorListener() {
                                @Override
                                public void onError(String error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Error de comunicaciones",
                                            Toast.LENGTH_SHORT).show();

                                    cleanEditText(username, password);
                                    setEnabledInput(username, password, true);
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Necesitas llenar ambos campos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setEnabledInput(EditText username, EditText password, boolean isEnabled) {
        username.setEnabled(isEnabled);
        password.setEnabled(isEnabled);
    }

    private void cleanEditText(EditText username, EditText password) {
        username.setText("");
        password.setText("");
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
