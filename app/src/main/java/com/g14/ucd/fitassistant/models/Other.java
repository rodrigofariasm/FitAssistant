package com.g14.ucd.fitassistant.models;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;

/**
 * Created by Natália on 17/10/2015.
 */

@ParseClassName("Other")
public class Other extends FitActivity {

    public String getDescription() {
        try{
            return fetchIfNeeded().getString("description");
        }catch (Exception e){}
        return "";
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public String getLocation() {
        try{
            return fetchIfNeeded().getString("location");
        }catch (Exception e){}
        return "";
    }

    public void setLocation(String location) {
        put("location", location);
    }



}
