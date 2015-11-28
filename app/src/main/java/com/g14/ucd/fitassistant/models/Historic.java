package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Natália on 17/10/2015.
 * This parseObject represents the historic of one day for Diet, gym or other exercise that the user recorded using the app. As done and not done.
 */
@ParseClassName("Historic")
public class Historic extends ParseObject {

    /*Date of this hitoric*/
    public String getDate() {
        try{
            return fetchIfNeeded().getString("date");
        }catch (Exception e){
            return "";
        }

    }

    public void setDate(String date) {
        put("date",date);
    }

    /*id of the event associated to this historic*/
    public String getEventId() {
        return getString("eventId");
    }

    public void setEventId(String eventId) {
        put("eventId",eventId);
    }

    public List<Object> getEventExercises() {
        return getList("eventExercises");
    }

    public void setEventExercises(ArrayList<ExerciseEvent> events) {
        put("eventExercises",events);
    }

    /*Map that stores the information about which meals in were ate and which not*/
    public Map<String,Boolean> getMealsAte(){
        return getMap("mealsAte");
    }

    public void setMealsAte(Map<String,Boolean> mealsAte){
        put("mealsAte",mealsAte);
    }

    /*Map that stores the information about which exercises in were done and which not*/
    public Map<String,Boolean> getExercisesDone(){
        return getMap("exercisesDone");
    }

    public void setExercisesDone(Map<String,Boolean> mealsAte){
        put("exercisesDone",mealsAte);
    }

    /*user of the historic*/
    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
}

