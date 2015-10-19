package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Leticia on 17/10/2015.
 */
@ParseClassName("Diet")
public class Diet extends ParseObject{

    public String name;
    public String description;
    public Set<Meal> meals;
    public boolean active;


    public Diet(String name, String description, Set<Meal> meals) {
        this.name = name;
        this.description = description;
        this.meals = meals;
    }

    public Diet(){
        this.meals = new HashSet<Meal>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
}
