package com.g14.ucd.fitassistant.models;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Leticia on 17/10/2015.
 */
public class Diet {

    public String name;
    public String description;
    public Set<Meal> meals;

    public Diet(String name, String description, Set<Meal> meals) {
        this.name = name;
        this.description = description;
        this.meals = meals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }
}
