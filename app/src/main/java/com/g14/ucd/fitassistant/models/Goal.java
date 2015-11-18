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

    public String getActual() {
        return getString("actual");
    }

    public void setActual(String actual) {
        put("actual",actual);
    }

    public String getGoal() {
        return getString("goal");
    }

    public void setGoal(String goal) {
        put("goal",goal);
    }

    public String getInterval() {
        return getString("interval");
    }

    public void setInterval(String interval) {
        put("interval",interval);
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
