package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing the Diet Parse entity and it's attributes
 */
@ParseClassName("Diet")
public class Diet extends ParseObject{

	/**
	 * Method to get the diet name
	 * */
    public String getName() {
        return getString("name");
    }

	/**
	 * Method to set the diet name
	 * */
    public void setName(String name) {
        put("name",name);
    }

	/**
	 * Method to get the diet description
	 * */
    public String getDescription() {
        return getString("description");
    }

	/**
	 * Method to set the diet description
	 * */
    public void setDescription(String description) {
        put("description",description);
    }

	/**
	 * Method to get the diet user
	 * */
    public ParseUser getUser(){
        return getParseUser("user");
    }

	/**
	 * Method to set the diet user
	 * */
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }

	/**
	 * Method to get the parse diet query
	 * */
    public static ParseQuery<Diet> getQuery() {
        return ParseQuery.getQuery(Diet.class);
    }

}
