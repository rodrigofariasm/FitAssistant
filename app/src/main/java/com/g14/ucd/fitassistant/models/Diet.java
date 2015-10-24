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
 * Created by Leticia on 17/10/2015.
 */
@ParseClassName("Diet")
public class Diet extends ParseObject{

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public boolean isActive() {
        return getBoolean("isActive");
    }

    public void setActive(boolean active) {
        put("isActive", active);
    }

    public List<Integer> getIdsMeals(){
        return getList("meals");
    }

    public void setMeals(List<Integer> meals){
        put("meals",meals);
    }

    public void addMeal(Integer ID){
        List<Integer> meals = getIdsMeals();
        if(meals == null){
            setMeals(new ArrayList<Integer>());
        }
        meals.add(ID);
    }

    public void removeMeal(Integer ID){
        List<Integer> meals = getIdsMeals();
        meals.remove(ID);
        setMeals(meals);
    }

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }

    public static ParseQuery<Diet> getQuery() {
        return ParseQuery.getQuery(Diet.class);
    }
}
