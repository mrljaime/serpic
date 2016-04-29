package com.tilatina.guardcheck.Utillities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jaime on 7/04/16.
 */
public class Preferences {
    public static String MYPREFERENCES = "GuardCheck";
    public static String USERID = "user_id";

    public static void putPreference(SharedPreferences sharedPreferences, String key, String value) {

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
        Log.d("JAIME...", String.format("Se ha agregado una preferencia a %s", MYPREFERENCES));
    }

    public static String getPreference(SharedPreferences sharedPreferences, String key, String defaultPrefer) {
        return sharedPreferences.getString(key, defaultPrefer);
    }

    public static void deletePreference(SharedPreferences sharedPreferences, String key) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
        Log.d("JAIME...", String.format("Se ha eliminado una preferencia a %s", MYPREFERENCES));
    }

    public static String getUTCDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date());
    }

    public static void downSize(Context context, Uri uri, int maxWidth) throws IOException {

        InputStream is;
        /** Primero obtenemos datos de la imagen sin cargar en memoria **/
        try {
            is = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException fnfe) {
            throw new IOException(String.format("File %s not found", uri.toString()));
        }
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        Log.d(MYPREFERENCES, String.format("witdh = %s, height = %s, mime='%s'", dbo.outWidth,
                dbo.outHeight, dbo.outMimeType));
        is.close();


        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(uri);
        /**
         * Comparamos contra el ancho máximo permitido
         */
        if (dbo.outWidth > maxWidth) {
            float ratio = ((float) dbo.outWidth) / ((float) maxWidth);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            Log.d("ImageUtil", String.format("comprimientoImagen ratio=%s",
                    ratio));

            /**
             * Obtener muestreo combinando cada n bits, sin perder dimensiones:
             */
            options.inSampleSize = (int) ratio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            Log.d("ImageUtil", "Imagen sin comprimir");
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();


        /**
         * Preparar el archivo para sobre-escribirlo
         */
        OutputStream stream = new FileOutputStream(uri.getPath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bsResized = byteArrayOutputStream.toByteArray();

        Log.d("ImageUtil", "Sobre escribiendo archivo...");
        stream.write(bsResized);
        stream.close();
        Log.d("ImageUtil", "Archivo sobreescrito");
    }
}
