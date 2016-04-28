package com.tilatina.guardcheck.Utillities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jaime on 28/04/16.
 */
public class LocationGPS {

    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = null;
        boolean gpsEnabled = true;

        if(null == locationManager) {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        }

        try {
            gpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(!gpsEnabled) {
            Toast.makeText(context, "Favor de encender el GPS", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static Location getCurrentLocation(Context context) throws SecurityException{

        LocationManager locationManager = (LocationManager)
                context.getSystemService(context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        LocationProvider locationProvider = locationManager.getProvider(locationManager.NETWORK_PROVIDER);
        locationProvider.meetsCriteria(criteria);
        Location location = locationManager.getLastKnownLocation(locationProvider.getName());

        if (null == location) {
            Toast.makeText(context, "No se encontró localización", Toast.LENGTH_SHORT).show();
            return null;
        }

        Log.d(Preferences.MYPREFERENCES, String.format("Provider = %s; Lat : %s; Lng : %s;",
                location.getProvider(), location.getLatitude(), location.getLongitude()));
        return location;
    }
}
