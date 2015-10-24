package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leticia on 17/10/2015.
 */
@ParseClassName("Meal")
public class Meal extends ParseObject{

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }

    public List<Integer> getOptionsID() {
        return getList("options");
    }

    public void setOptions(List<Integer> options) {
        put("options",options);
    }

    public void addOption(int optionID){
        List<Integer> options = getOptionsID();
        if(options == null){
            setOptions(new ArrayList<Integer>());
        }
        options.add(optionID);
    }

    public void removeOption(int optionID){
        List<Integer> options = getOptionsID();
        options.remove(optionID);
        setOptions(options);
    }

    public int getDietID(){
        try {
            return fetchIfNeeded().getInt("dietID");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void setDietID(int dietID){
        put("dietID", dietID);
    }

    public void setUser(ParseUser user) {
        if (user != null) {
            put("user", user);
        }
    }

    public static ParseQuery<Meal> getQuery() {
        return ParseQuery.getQuery(Meal.class);
    }
}
