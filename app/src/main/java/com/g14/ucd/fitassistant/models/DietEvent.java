package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Natália on 16/10/2015.
 */
@ParseClassName("DietEvent")
public class DietEvent extends ParseObject{

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name",name);
    }

    public String getDietId() {
        return getString("DietId");
    }

    public void setDietId(String dietId) {
        put("DietId",dietId);
    }

    public List<Integer> getWeekDays(){
        return getList("weekDays");
    }

    public void setWeekDays( List<Integer> weekDays){
        put("weekDays",weekDays);
    }


    public void setTimes(Map<String,Date> times){
        put("times", times);
    }

    public Map<String,Date> getTimes(){
        return getMap("times");
    }

    public void addMealTime(Date date, int mealType){
        Map<String,Date> times = getTimes();

        if(times == null){
            times = new HashMap<String,Date>();
        }
        times.put(Integer.toString(mealType),date);
        setTimes(times);
    }

    public ParseUser getUser(){
        return getParseUser("user");
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
