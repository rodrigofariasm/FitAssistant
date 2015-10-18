package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 17/10/2015.
 */
public class Other extends Activity{

    public String description;
    public String location;

    public Other(String name, int type, String description,String location){
        this.name = name;
        this.type = type;
        this.description = description;
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
