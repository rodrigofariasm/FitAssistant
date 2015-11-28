package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Class representing the Exercise Parse entity and it's attributes
 * which relates to each exercise that the user can take on the gym
 */
@ParseClassName("Exercise")
public class Exercise extends ParseObject{

	/**
	 * Method to get the amount of set of the exercise
	 * */
    public int getSections() {
        return getInt("sections");
    }

	/**
	 * Method to set the amount of set of the exercise
	 * */
    public void setSections(int sections) {
       put("sections", sections);
    }

	/**
	 * Method to get the exercise name
	 * */
    public String getName() {
        return getString("name");
    }

	/**
	 * Method to set the exercise name
	 * */
    public void setName(String name) {
        put("name",name);
    }

	/**
	 * Method to get the amount of repetitions of the exercise
	 * */
    public int getRepetitions() {
        return getInt("repetitions");
    }

	/**
	 * Method to set the amount of repetitions of the exercise
	 * */
    public void setRepetitions(int repetitions) {
        put("repetitions",repetitions);
    }

	/**
	 * Method to get the gym activity ID
	 * */
    public Gym getActivityID() {
        try {
            return (Gym) getParseObject("activityID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Gym();
    }

	/**
	 * Method to set the gym activity ID
	 * */
    public void setActivityID(Gym value) {
        put("activityID", value);
    }
    
    /**
	 * Method to get the exercise user
	 * */
    public ParseUser getUser(){
        return getParseUser("user");
    }

	/**
	 * Method to set the exercise user
	 * */
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
}
