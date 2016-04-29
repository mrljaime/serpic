package com.tilatina.guardcheck.Utillities;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.speech.RecognitionService;
import android.text.TextUtils;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    public interface MakeOkManualListener {
        void onSuccess(String response);
        void onError(String error);
    }

    public interface ChangePositionListener {
        void onSuccess(String response);
        void onError(String error);
    }

    public interface onFileUploadListener {
        int fileUploaded(String response);
    }

    public interface onErrorListener {
        int onError();
    }

    public interface sendNoveltyListener {
        void onSuccess(String response);
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

    public static void makeOkManualAction(Context context, final String user, final String elementId,
                                          final WebService.MakeOkManualListener makeOkManualListener) {
        String url = String.format("%s/ws/guard/okManualCheck", DEV_URL);

        StringRequest makeOkManual = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Preferences.MYPREFERENCES, "Responde = " + response);
                makeOkManualListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeOkManualListener.onError("Error en la conexión");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("element", elementId);
                return params;
            }
        };

        makeOkManual.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(makeOkManual);
    }

    public static void changePositionAction(Context context, final String user, final String elementId,
                                            final String lat, final String lng,
                                            final WebService.ChangePositionListener changePositionListener) {

        String url = String.format("%s/ws/guard/isHerePosition", DEV_URL);

        StringRequest changePosition = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(Preferences.MYPREFERENCES, "Response = " + response);
                    changePositionListener.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d(Preferences.MYPREFERENCES, "Error");
                    changePositionListener.onError("Error en la petición");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("element", elementId);
                params.put("lat", lat);
                params.put("lng", lng);

                return params;
            }
        };

        changePosition.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(changePosition);

    }


    /**
     * Uploads file to server in a separated thread. If file can not be uploaded signals a
     * communications error through the onErrorListener. NOTE: As this method runs under an
     * AsynTask, listeners are required to issue a new Runnable task in order to show UI
     * messages.
     * @param fileTitle File name that will be reported to the server.
     * @param path Whole file path within the mobile equipment.
     * @param headers Headers to be sent along the request.
     * @param requestParams Request params.
     * @param onFileUploadedListener Listener to be called upon upload success.
     * @param onErrorListener Listener that signals upload failure.
     */
    public static void uploadFile(final String fileTitle, final String path, final String element,
                                  final Map<String, String> headers,
                                  final Map<String, String> requestParams,
                                  final onFileUploadListener onFileUploadedListener,
                                  final onErrorListener onErrorListener, final Context context) {

        Log.d("FileUploader", String.format("path='%s'", path));

        final File sourceFile = new File(path);
        List<String> paramList = new ArrayList<String>();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            paramList.add(param.getKey() + "=" + param.getValue());
        }
        final String queryString = TextUtils.join("&", paramList);

        if (!sourceFile.isFile()) {
            Log.e("FileUploader", String.format("Source File '%s' does not exist", path));
            onErrorListener.onError();
        } else {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        Log.d(Preferences.MYPREFERENCES, "Intentando mandar.... antes de conexión");
                        HttpURLConnection conn = null;
                        DataOutputStream dos = null;
                        String lineEnd = "\r\n";
                        String twoHyphens = "--";
                        String boundary = "*****";
                        int bytesRead, bytesAvailable, bufferSize;
                        byte[] buffer;
                        int maxBufferSize = 1 * 1024 * 1024;


                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(String.format("%s/%s/noveltyMonitor?%s",
                                DEV_URL, element, queryString));
                        Log.d("FileUploader", String.format("Uploading to %s", url.toString()));

                        // Open a HTTP  connection to  the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("file", fileTitle);
                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                conn.setRequestProperty(entry.getKey(), entry.getValue());
                            }
                        }

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                                + fileTitle + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of  maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();


                        Log.d("FileUploader", "HTTP Response is : "
                                + serverResponseMessage + ": " + serverResponseCode);


                        //close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        if (serverResponseCode == 200) {

                            //Para ver la respuesta
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String output;
                            while ((output = br.readLine()) != null) {
                                sb.append(output);
                            }
                            Log.d("SERVER RESPONSE ----- ", sb.toString());


                            onFileUploadedListener.fileUploaded(sb.toString());
                        } else {
                            Log.e("FileUploader", String.format("Response code %s (%s) from '%s'",
                                    serverResponseCode, serverResponseMessage, url.toString()));
                            onErrorListener.onError();
                        }

                    } catch (MalformedURLException ex) {
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                        onErrorListener.onError();
                    } catch (Exception e) {
                        Log.e("Upload file to server", "error: " + e.getMessage(), e);
                        onErrorListener.onError();
                    }
                    return null;
                }

            }.execute();

        } // End else block
    }


    public static void sendNoveltyWithOutPicture(Context context, final Map<String, String> params, String element,
                                                 final sendNoveltyListener sendNoveltyListener) {

        String url = String.format("%s/%s/noveltyWithOutMonitor", DEV_URL, element);
        Log.d(Preferences.MYPREFERENCES, String.format("URL = %s", url));

        StringRequest sendNovelty = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Preferences.MYPREFERENCES, response);
                sendNoveltyListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendNoveltyListener.onError("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };


        sendNovelty.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(sendNovelty);

    }

}
