package com.example.tripit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.mappls.sdk.maps.geometry.LatLng;

public class LocationUtils {

    // Define constants for minimum time interval and minimum distance for location updates
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1 second
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1.0f; // 1 meter

    // Get current latitude and longitude as a LatLng object
    public static LatLng getCurrentLatLng(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return null;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();
            return new LatLng(latitude, longitude);
        } else {
            // Handle the case where the last known location is not available
            return null;
        }
    }
}
