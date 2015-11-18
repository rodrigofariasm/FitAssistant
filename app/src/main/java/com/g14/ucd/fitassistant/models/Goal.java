package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Leticia on 17/11/2015.
 */
@ParseClassName("Goal")
public class Goal extends ParseObject{
    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type",type);
    }

    public int getActual() {
        return getInt("actual");
    }

    public void setActual(int actual) {
        put("actual",actual);
    }

    public int getDesired() {
        return getInt("desired");
    }

    public void setDesired(int desired) {
        put("desired",desired);
    }

    public int getInterval() {
        return getInt("interval");
    }

    public void setInterval(int interval) {
        put("interval",interval);
    }

    public String getIntervalUnit() {
        return getString("interval_unit");
    }

    public void setIntervalUnit(String interval_unit) {
        put("interval_unit",interval_unit);
    }

    public boolean isActive() {
        return getBoolean("isActive");
    }

    public void setActive(boolean active) {
        put("isActive", active);
    }

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }

    public static ParseQuery<Goal> getQuery() {
        return ParseQuery.getQuery(Goal.class);
    }
}
