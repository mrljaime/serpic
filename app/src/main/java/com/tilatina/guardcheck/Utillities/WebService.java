package com.tilatina.guardcheck.Utillities;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;


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
import java.util.List;

/**
 * Created by jaime on 11/04/16.
 */
public class WebService {
    public static String DEV_URL = "http://192.168.4.174:8000";

    public interface LoginSuccessListener {
        void onSuccess(User user);
    }

    public interface LoginErrorListener {
        void onError(String error);
    }

    public interface ServicesSuccessListener {
        void onSuccess(List<ServiceStatus> services);
    }

    public interface ServicesErrorListener {
        void onError(String error);
    }


    public void loginAction(String username, String password, Context context,
                           final LoginSuccessListener loginSuccessListener,
                           final LoginErrorListener loginErrorListener) {

        String url = String.format("%s/ws/guard/login?_username=%s&_password=%s", DEV_URL,
                URLEncoder.encode(username), URLEncoder.encode(password));

        Log.d("GUARD_CHECK...", String.format("URL for login actio = %s", url));

        StringRequest loginAction = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new User();

                try {
                    JSONObject userFromWS = new JSONObject(response);
                    user.setId(userFromWS.getInt("id"));
                    user.setName(userFromWS.getString("name"));
                    loginSuccessListener.onSuccess(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginErrorListener.onError("No se pudo completar la petición");
                Log.d("GUARD_CHECK...", String.format("LOGIN ERROR = %s", error.getCause()));
            }
        });

        loginAction.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(loginAction);
    }

    public void getServicesAction(String user, String status, String orderBy, String sort, String search,
                                  ServicesSuccessListener servicesSuccessListener,
                                  ServicesErrorListener servicesErrorListener) {

    }

}
