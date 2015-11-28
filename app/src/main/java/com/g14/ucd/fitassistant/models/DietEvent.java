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
 * Class representing the Diet Event Parse entity and it's attributes
 */
@ParseClassName("DietEvent")
public class DietEvent extends ParseObject{
	
	/**
	 * Method to get the diet event name
	 * */
    public String getName(){
        return getString("name");
    }
	
	/**
	 * Method to set the diet event name
	 * */
    public void setName(String name){
        put("name",name);
    }

	/**
	 * Method to get the diet event ID
	 * */
    public String getDietId() {
        return getString("DietId");
    }

	/**
	 * Method to set the diet event ID
	 * */
    public void setDietId(String dietId) {
        put("DietId",dietId);
    }

	/**
	 * Method to get the list of the days of the week that the diet 
	 * event will happen
	 * */
    public List<Integer> getWeekDays(){
        return getList("weekDays");
    }

	/**
	 * Method to set the list of the days of the week that the diet 
	 * event will happen
	 * */
    public void setWeekDays( List<Integer> weekDays){
        put("weekDays",weekDays);
    }

	/**
	 * Method to set the map responsable for recording the meals times
	 * */
    public void setTimes(Map<String,Date> times){
        put("times", times);
    }

	/**
	 * Method to get the map responsable for recording the meals times
	 * */
    public Map<String,Date> getTimes(){
        return getMap("times");
    }

	/**
	 * Method that adds a meal and its time on the map record.
	 * */
    public void addMealTime(Date date, int mealType){
        Map<String,Date> times = getTimes();

        if(times == null){
            times = new HashMap<String,Date>();
        }
        times.put(Integer.toString(mealType),date);
        setTimes(times);
    }
    
    /**
	 * Method to get the diet event user
	 * */
    public ParseUser getUser(){
        return getParseUser("user");
    }

	/**
	 * Method to set the diet event user
	 * */
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
    
    /**
	 * Method to set the diet event notification
	 * */
    public void setNotification(int i ){
        put("notificationGroup", i);
    }

	/**
	 * Method to get the diet event notification
	 * */
    public int getNotification(){
        try{
            return fetchIfNeeded().getInt("notificationGroup");
        }catch(ParseException e){

        }
        return 0;
    }

	/**
	 * Method to get the information related to whether the diet event will 
	 * repeat or not
	 * */
    public boolean getRepeat(){
        return getBoolean("repeat");
    }
    
    /**
	 * Method to set the information related to whether the diet event will 
	 * repeat or not
	 * */
    public void setRepeat(boolean repeat){
        put("repeat", repeat);
    }
}
