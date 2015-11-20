package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Natália on 17/10/2015.
 */
@ParseClassName("Exercise")
public class Exercise extends ParseObject{

    public int getSections() {
        return getInt("sections");
    }

    public void setSections(int sections) {
       put("sections", sections);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public int getRepetitions() {
        return getInt("repetitions");
    }

    public void setRepetitions(int repetitions) {
        put("repetitions",repetitions);
    }

    public String getActivityID() {
        try {
            return fetchIfNeeded().getString("activityID");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "erro";
    }

    public void setActivityID(String value) {
        put("activityID", value);
    }
    public void setUser(ParseUser user) {
        put("user", user);
    }
}
