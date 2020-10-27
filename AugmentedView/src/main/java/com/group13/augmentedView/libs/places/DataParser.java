package com.group13.augmentedView.libs.places;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baptiste
 */

public class DataParser {
    public static final String TAG = "DataParser";

    /**
     *
     * @param googlePlaceJson that obtains with the method parse
     * @return the HashMap<String, String> where information about the building
     */
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String isOpened = "no opening hours";
        String rating = "no rating";
        String iconUrl = "--NA--";
        String phoneNumber = "not available";
        String website = "no website";


        try {
            if (!googlePlaceJson.isNull("name"))
                placeName = googlePlaceJson.getString("name");

            if(!googlePlaceJson.isNull("opening_hours")) {
                JSONObject jOpeningObject = googlePlaceJson.getJSONObject("opening_hours");
                if(jOpeningObject != null) {
                    isOpened = jOpeningObject.getString("open_now");
                }
            }
            if(!googlePlaceJson.isNull("rating"))
                rating = googlePlaceJson.getString("rating");
            if(!googlePlaceJson.isNull("icon"))
                iconUrl = googlePlaceJson.getString("icon");
            if(!googlePlaceJson.isNull("formatted_phone_number"))
                phoneNumber = googlePlaceJson.getString("formatted_phone_number");
            if(!googlePlaceJson.isNull("website"))
                website = googlePlaceJson.getString("website");

            googlePlaceMap.put("name", placeName);
            googlePlaceMap.put("isOpened", isOpened);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("icon", iconUrl);
            googlePlaceMap.put("phone", phoneNumber);
            googlePlaceMap.put("website", website);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;

    }

    /**
     *
     * @param jsonData it's the string obtained by the google request
     * @return if the request worked  it returns the HashMap< String, String> where information about the building
     *          else returns the HashMap with 'not found' fields
     */
    public HashMap<String, String> parse(String jsonData){
        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONObject toParse = null;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
            toParse = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(toParse != null) {
            return getPlace(toParse);
        }
        else {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", "not foud");
            map.put("isOpened", "not foud");
            map.put("rating", "not foud");
            map.put("icon", "not foud");
            map.put("phone", "not foud");
            map.put("website", "not foud");
            return map;
        }
    }
}
