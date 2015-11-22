package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;

/**
 * Created by Natália on 17/10/2015.
 */

@ParseClassName("Other")
public class Other extends FitActivity {

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public String getLocation() {
        return (String) get("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

}
