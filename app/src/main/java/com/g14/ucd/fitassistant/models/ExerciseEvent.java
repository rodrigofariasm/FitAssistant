package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leticia on 21/11/2015.
 */
@ParseClassName("ExerciseEvent")
public class ExerciseEvent extends ParseObject{


    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public Date getTime() {
        return getDate("time");
    }

    public void setTime(Date time) {
        put("time",time);
    }

    public String getExerciseID() {
        return getString("exerciseID");
    }

    public void setExerciseID(String exerciseID) {
        put("exerciseID",exerciseID);
    }

    public List<Integer> getWeekdays() {
        return getList("weekdays");
    }

    public void setWeekdays(List<Integer> weekdays) {
        put("weekdays", weekdays);
    }

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public boolean getRepeat(){
        return getBoolean("repeat");
    }

    public void setRepeat(boolean repeat){
        put("repeat", repeat);
    }
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
    public void setNotification(int i ){
        put("notificationGroup", i);
    }

    public int getNotification(){
        try{
            return fetchIfNeeded().getInt("notificationGroup");
        }catch(ParseException e){

        }
        return 0;
    }
}
