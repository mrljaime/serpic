package com.tilatina.guardcheck.Utillities;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jaime on 7/04/16.
 */
public class Preferences {
    public static String MYPREFERENCES = "GuardCheck";

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
}
