package com.group13.augmentedView.libs.places;

import android.os.AsyncTask;
import android.util.Log;

import com.group13.augmentedView.UnityPlayerActivity;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Maxime Beugoms
 * @author Florian Duprez
 * @author Baptiste Lapiere
 * @author Martin Meerts
 *
 * This call manage the google request and the storage buidling's information
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private static final String TAG = "GetNearbyPlacesData";

    private String googlePlacesData;
    private PlaceInfo placeInfo;
    private boolean finishState;

    @Override
    protected String doInBackground(Object... objects) {
        placeInfo = (PlaceInfo) objects[0];
        String url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException :"+e.getMessage());
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        HashMap<String, String> nearbyPlace;
        DataParser parser = new DataParser();
        nearbyPlace = parser.parse(s);
        storeNearbyPlace(nearbyPlace);

        UnityPlayerActivity.setPlaceInfo(placeInfo);
        finishState = true;
    }

    /**
     * Initialise the placeInfo object with the hash map
     * @param googlePlace it's hash map that contains information
     */
    private void storeNearbyPlace(HashMap<String, String> googlePlace)
    {
        if (googlePlace != null) {
            if(googlePlace.get("name") == null) {placeInfo = null;}
            placeInfo.setName(googlePlace.get("name"));
            if(googlePlace.get("isOpened") == null) {placeInfo = null;}
            placeInfo.setIsOpened(googlePlace.get("isOpened"));
            if(googlePlace.get("rating") == null) {placeInfo = null;}
            placeInfo.setRating(googlePlace.get("rating"));
            if(googlePlace.get("icon") == null) {placeInfo = null;}
            placeInfo.setIcon(googlePlace.get("icon"));
            if(googlePlace.get("phone") == null) {placeInfo = null;}
            placeInfo.setPhone(googlePlace.get("phone"));
            if(googlePlace.get("website") == null) {placeInfo = null;}
            placeInfo.setWebsite(googlePlace.get("website"));
        }
    }

    /**
     *
     * @return the state of the asynchronous task
     */
    public boolean isAvailable(){
        return finishState;
    }

}
