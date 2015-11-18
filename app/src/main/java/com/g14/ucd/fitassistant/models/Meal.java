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
public class Meal extends ParseObject implements Comparable{

    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }

    public List<String> getOptions() {
        return getList("options");
    }

    public void setOptions(List<String> options) {
        put("options", options);
    }

    public void addOption(String option){
        List<String> options = getOptions();
        if(options == null){
            options = new ArrayList<String>();
         }
        options.add(option);
        setOptions(options);
    }

    public void removeOption(String optionID){
        List<String> options = getOptions();
        options.remove(optionID);
        setOptions(options);
    }

    public String getDietID(){
        try {
            return fetchIfNeeded().getString("dietID");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDietID(String dietID){
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


    @Override
    public int compareTo(Object another) {
        if(another instanceof Meal) {
            Meal other = (Meal) another;
            if (this.getType() < other.getType()) {
                return -1;
            }
            if (this.getType() > other.getType()) {
                return 1;
            }
        }
        return 0;
    }
}
