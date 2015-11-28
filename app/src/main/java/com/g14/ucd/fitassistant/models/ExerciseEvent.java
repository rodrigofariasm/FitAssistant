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
 * Class representing the Exercise Event Parse entity and it's attributes
 */
@ParseClassName("ExerciseEvent")
public class ExerciseEvent extends ParseObject{

	/**
	 * Method to get the exercise event name
	 * */
    public String getName() {
        return getString("name");
    }

	/**
	 * Method to set the exercise event name
	 * */
    public void setName(String name) {
        put("name", name);
    }

	/**
	 * Method to get the exercise event time
	 * */
    public Date getTime() {
        return getDate("time");
    }

	/**
	 * Method to set the exercise event time
	 * */
    public void setTime(Date time) {
        put("time",time);
    }

	/**
	 * Method to get the exercise event ID
	 * */
    public String getExerciseID() {
        return getString("exerciseID");
    }

	/**
	 * Method to set the exercise event ID
	 * */
    public void setExerciseID(String exerciseID) {
        put("exerciseID",exerciseID);
    }

	/**
	 * Method to get the list of the days of the week that the exercise 
	 * event will happen
	 * */
    public List<Integer> getWeekdays() {
        return getList("weekdays");
    }

	/**
	 * Method to set the list of the days of the week that the exercise 
	 * event will happen
	 * */
    public void setWeekdays(List<Integer> weekdays) {
        put("weekdays", weekdays);
    }

	/**
	 * Method to get the exercise event user
	 * */
    public ParseUser getUser(){
        return getParseUser("user");
    }

	/**
	 * Method to get the information related to whether the exercise event will 
	 * repeat or not
	 * */
    public boolean getRepeat(){
        return getBoolean("repeat");
    }

	/**
	 * Method to set the information related to whether the exercise event will 
	 * repeat or not
	 * */
    public void setRepeat(boolean repeat){
        put("repeat", repeat);
    }
    
	/**
	 * Method to set the exercise event user
	 * */
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
    
    /**
	 * Method to set the exercise event notification
	 * */
    public void setNotification(int i ){
        put("notificationGroup", i);
    }

	/**
	 * Method to get the exercise event notification
	 * */
    public int getNotification(){
        try{
            return fetchIfNeeded().getInt("notificationGroup");
        }catch(ParseException e){

        }
        return 0;
    }
}
