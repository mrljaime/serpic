package com.tilatina.guardcheck;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tilatina.guardcheck.Utillities.NoveltyThumbnail;
import com.tilatina.guardcheck.Utillities.Preferences;
import com.tilatina.guardcheck.Utillities.ThumbnailAdapter;
import com.tilatina.guardcheck.Utillities.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoveltyActivity extends AppCompatActivity {

    /**
     * Global variables
     */

    Context me = this;
    Spinner stateColorSpin = null;
    ImageButton takePicture = null;
    ListView thumbnailView = null;
    Button sendNovelty = null;
    EditText description = null;

    String dateString = null;
    String element = null;
    Uri mCurrentPhotoPath = null;
    ArrayList<NoveltyThumbnail> noveltyThumbnails = new ArrayList<>();
    ThumbnailAdapter thumbnailAdapter = new ThumbnailAdapter(me, noveltyThumbnails);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novelty);

        /**
         * Extras from intent
         */
        Bundle bundle = getIntent().getExtras();
        element = String.format("%s",bundle.getInt("element"));


        /**
         * Resource for spinner of state color
         */
        String[] stateColor = {"Verde", "Amarillo", "Rojo"};
        ArrayAdapter<String> stateColorAdapter = new ArrayAdapter<String>(me,
                R.layout.support_simple_spinner_dropdown_item, stateColor);

        /**
         * Set adapter to spinner
         */
        stateColorSpin = (Spinner) findViewById(R.id.stateColorSpinner);
        stateColorSpin.setAdapter(stateColorAdapter);

        /**
         * Take picture button action
         */
        takePicture = (ImageButton) findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                dateString = Preferences.getUTCDateTime();

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File picture = null;
                    picture = new File(Environment.
                            getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dateString + ".jpg");
                    mCurrentPhotoPath = Uri.fromFile(picture);
                    Log.d(Preferences.MYPREFERENCES, "Get camera intent");
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        /**
         * Set adapter tu listView
         */
        thumbnailView = (ListView) findViewById(R.id.thumbnailListView);
        thumbnailView.setAdapter(thumbnailAdapter);

        /**
         * Edit text initialize
         */
        description = (EditText) findViewById(R.id.noveltyDescription);


        /**
         * Send novelty to server
         */
        sendNovelty = (Button) findViewById(R.id.sendNoveltyButton);
        sendNovelty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (description.getText().toString().trim().length() != 0) {
                    dateString = Preferences.getUTCDateTime();
                    final ProgressDialog progressDialog = new ProgressDialog(me);
                    progressDialog.setMessage("Enviando");

                    String stateColorValue = stateColorSpin.getSelectedItem().toString();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("description", URLEncoder.encode(description.getText().toString()));
                    params.put("captureDate", URLEncoder.encode(dateString));
                    params.put("colorStatus", transfornStateColorValue(stateColorValue));
                    progressDialog.show();

                    sendNoveltyToServer(me, params, noveltyThumbnails, new onSuccessResultSend() {
                        @Override
                        public void returnValue(int value) {

                            /**
                             * Success
                             */
                            if (200 == value) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.hide();
                                        finish();
                                        Toast.makeText(me, "Novedad enviada", Toast.LENGTH_SHORT).show();

                                        return;
                                    }
                                });
                            }

                            /**
                             * Service not found
                             */
                            if (320 == value) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.hide();
                                        Intent intent = new Intent();
                                        intent.setClass(me, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(me, "El servicio no fue encontrado, contacta a tu supervisor",
                                                Toast.LENGTH_SHORT).show();

                                        return;
                                    }
                                });

                            }

                            /**
                             * Error in the request
                             */
                            if (500 == value) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.hide();
                                        Toast.makeText(me, "Error de comunicaciones",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            /**
                             * Error
                             */
                            if (400 == value) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.hide();
                                        Toast.makeText(me, "Error de comunicaciones",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });


                } else {
                    description.setError("El campo es obligatorio");
                }
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath.getPath());
            NoveltyThumbnail noveltyThumbnail = new NoveltyThumbnail(image, mCurrentPhotoPath.getPath());
            noveltyThumbnails.add(noveltyThumbnail);
            thumbnailAdapter.notifyDataSetChanged();
            try {
                Preferences.downSize(this, mCurrentPhotoPath, 250);
            }catch(IOException e) {
                e.printStackTrace();
            }

            takePicture.setEnabled(false);
        }
    }

    private int sendNoveltyToServer(final Context context, Map<String, String> params,
                                    ArrayList<NoveltyThumbnail> noveltyThumbnails,
                                    final NoveltyActivity.onSuccessResultSend listener) {
        /**
         * Check if count of noveltyThumbnails is larger than 1, if is true, make a request with picture,
         * otherwise, make a request only with novelty text and color state of the event.
         * We consider than noveltyThumbnails is limit to 1 item
         */
        if (0 != noveltyThumbnails.size()) {
            Bitmap image = noveltyThumbnails.get(0).getThumbnail();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

            /**
             * Make a call to webService class for make the request.
             */

            WebService.uploadFile("uploadFile", noveltyThumbnails.get(0).getPath(), element, null, params, new WebService.onFileUploadListener() {
                @Override
                public int fileUploaded(String response) {
                    Log.d(Preferences.MYPREFERENCES, String.format("RESPONSE = %s", response));
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (200 == jsonResponse.getInt("code")) {
                            /**
                             * Return 200 for success
                             */
                            listener.returnValue(200);
                            return 200;
                        } else if (302 == jsonResponse.getInt("code")) {
                            /**
                             * Return 302 because the service was not found in server
                             */
                            listener.returnValue(302);
                            return 302;
                        } else if (404 == jsonResponse.getInt("code")) {
                            listener.returnValue(404);
                            return 404;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return 0;
                }
            }, new WebService.onErrorListener() {
                @Override
                public int onError() {
                    /**
                     * Return 500 for error
                     */
                    listener.returnValue(500);
                    return 500;
                }
            }, context);

        } else {
            WebService.sendNoveltyWithOutPicture(context, params, element, new WebService.sendNoveltyListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (200 == jsonResponse.getInt("code")) {
                            listener.returnValue(200);
                        } else {
                            listener.returnValue(302);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    listener.returnValue(500);
                }
            });
        }

        return 0;
    }

    private interface onSuccessResultSend{
        void returnValue(int value);
    }

    private String transfornStateColorValue(String color) {
        if ("Amarillo".equals(color)) {
            return "Y";
        }

        if ("Rojo".equals(color)) {
            return "R";
        }

        if ("Verde".equals(color)) {
            return "G";
        }

        return "";
    }

}
