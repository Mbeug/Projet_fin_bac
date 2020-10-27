package com.group13.augmentedView.libs.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


/**
 *
 * @author Maxime Beugoms
 * @author Florian Duprez
 * @author Baptiste Lapiere
 * @author Martin Meerts
 *
 * This class get localisation's informations
 */
public class MyLocation implements LocationListener {

    private static final String TAG = "LocalisationListener";
    private double latitude;
    private double longitude;

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            setLatitude(location.getLatitude());
            setLongitude(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public double getLatitude() {
        return latitude;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
