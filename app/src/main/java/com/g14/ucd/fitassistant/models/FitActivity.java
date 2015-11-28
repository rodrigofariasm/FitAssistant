package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Class representing the FitActivity Parse entity and it's attributes
 * which is the super class of the Gym and Other entities
 */
@ParseClassName("FitActivity")
public class FitActivity extends ParseObject {

	/**
	 * Method to get the FitActivity name
	 * */
    public String getName() {
        return getString("name");
    }

	/**
	 * Method to set the FitActivity name
	 * */
    public void setName(String name) {
        put("name",name);
    }

	/**
	 * Method to get the FitActivity type (gym or other)
	 * */
    public int getType() {
        return getInt("type");
    }
    
    /**
	 * Method to set the FitActivity type (gym or other)
	 * */
    public void setType(int type1){
        put("type",type1);
    }
    
    /**
	 * Method to set the FitActivity user
	 * */
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
    
    /**
	 * Method to get the FitActivity user
	 * */
    public ParseUser getUser(){
        return getParseUser("user");
    }
}
