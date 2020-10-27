package com.group13.augmentedView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.group13.augmentedView.libs.location.MyLocation;
import com.group13.augmentedView.libs.places.GetNearbyPlacesData;
import com.group13.augmentedView.libs.places.PlaceInfo;
import com.unity3d.player.UnityPlayer;

import io.fabric.sdk.android.Fabric;

/**
 * @author Maxime Beugoms
 * @author Florian Duprez
 * @author Baptiste Lapiere
 * @author Martin Meerts
 *
 * This class is the main android activity and
 * the structure of this is generate by unity when we export the project
 */
public class UnityPlayerActivity extends Activity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    private static final String TAG = "UnityPlayerActivity";

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Parameters for Google Places request
    private static final int PROXIMITY_RADIUS = 1000;
    private static final String PLACES_API_KEY = "AIzaSyDPsGbVVfQetwFgdiY-jqpeCXxnchh9RII";
    private static PlaceInfo placeInfo;

    // Parameters for localisation request
    private static final int REQUEST_LOCATION_PERMISSION = 200;
    private static double latitude=50.6685758;
    private static double longitude=4.6194023;


    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();


        double [] myLoc = setLocalisationInfo();
        if (myLoc==null)
            myLoc = setLocalisationInfo();
        else{
            if (myLoc[0]!=0 || myLoc[1]!=0){
                latitude = myLoc[0];
                longitude= myLoc[1];
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    /**
     *  Method called in unity's script
     * @param place it's the name of the building on which the google request must be done
     * @return a array of String who contains infos for the unity canvas
     */
    public static String[] onExplode(String place) {

        final Object[] dataTransfer = new Object[2];
        dataTransfer[0] = new PlaceInfo();
        dataTransfer[1] = buildURL(latitude, longitude, place);

        GetNearbyPlacesData getter = new GetNearbyPlacesData();
        getter.execute(dataTransfer);
        while (!getter.isAvailable()){}

        return placeInfo.toStringArray();
    }

    public static void setPlaceInfo(PlaceInfo placeInfo) {
        UnityPlayerActivity.placeInfo = placeInfo;
    }

    /**
     *
     * @param lat it's the user's latitude
     * @param lng it's the user's longitude
     * @param keyword it's the keyword for the google request, search
     * @return a String that contains the url for the request
     */
    private static String buildURL(double lat, double lng, String keyword) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + lat + "," + lng);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&keyword=" + keyword);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + PLACES_API_KEY);

        return googlePlaceUrl.toString();
    }

    /**
     *
     * @return initialisation of parameters latitude and longitude
     */
    private double[] setLocalisationInfo() {
        LocationManager myManager;
        MyLocation loc = new MyLocation();
        myManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Condition to check if the permission is ready
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);
            return null;
        }
        assert myManager != null;
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, loc);
        Location currentPos = myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentPos == null){
            Log.e(TAG,"current position is null");
        }

        if (currentPos != null) {
            return new double[] {currentPos.getLatitude(), currentPos.getLongitude()};
        }
        return new double[] {0, 0};
    }


    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API client connection successful.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "API client connection failed.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "API client connection suspended.");
    }
}
