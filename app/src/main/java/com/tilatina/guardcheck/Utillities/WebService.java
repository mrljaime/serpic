package com.tilatina.guardcheck.Utillities;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tilatina.guardcheck.Dao.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaime on 11/04/16.
 */
public class WebService {
    public static String DEV_URL = "http://192.168.3.46:8090";

    public interface LoginSuccessListener {
        void onSuccess(String response);
    }

    public interface LoginErrorListener {
        void onError(String error);
    }

    public interface ServicesSuccessListener {
        void onSuccess(String response);
    }

    public interface ServicesErrorListener {
        void onError(String error);
    }


    public void loginAction(final String username, final String password, final String domain, Context context,
                            final LoginSuccessListener loginSuccessListener,
                            final LoginErrorListener loginErrorListener) {

        String url = String.format("%s/ws/guard/login", DEV_URL);

        Log.d("GUARD_CHECK...", String.format("URL for login actio = %s", url));

        StringRequest loginAction = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Preferences.MYPREFERENCES, "Response = " + response);
                loginSuccessListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginErrorListener.onError("No se pudo completar la petición");
                Log.d("GUARD_CHECK...", String.format("LOGIN ERROR = %s", error.getCause()));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("_username", String.format("%s#%s.crea.tilatina.com:8000", username, domain));
                params.put("_password", password);
                return params;
            }
        };
        loginAction.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(loginAction);
    }

    public static void getServicesAction(Context context, final String user, final String status,
                                         final String orderBy, final String sort, final String search,
                                         final String lat, final String lng,
                                         final ServicesSuccessListener servicesSuccessListener,
                                         final ServicesErrorListener servicesErrorListener) {

        String url = String.format("%s/ws/guard/getServiceList", DEV_URL);

        StringRequest servicesRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Preferences.MYPREFERENCES, "Response service action: " + response);
                servicesSuccessListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                servicesErrorListener.onError("Error de comunicaciones");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("status", status);
                params.put("orderBy", orderBy);
                params.put("sort", sort);
                params.put("search", search);

                return params;
            }
        };

        servicesRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(servicesRequest);
    }

}
