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
 * Parse Object que represent one of the meal that the user can register of they diet.
 */
@ParseClassName("Meal")
public class Meal extends ParseObject implements Comparable{

    /*Type of the diet, maped on the types strings in the MealEnum
    * */
    public int getType() {
        return getInt("type");
    }

    public void setType(int type) {
        put("type",type);
    }

    /*List of options that the user can eat in the meal*/
    public List<String> getOptions() {
        return getList("options");
    }

    public void setOptions(List<String> options) {
        put("options", options);
    }

    /*Méthod to add a new option to the list of meals*/
    public void addOption(String option){
        List<String> options = getOptions(); //get the list of options from parse
        if(options == null){ //if the list doesn't exists yet
            options = new ArrayList<String>(); //create one
         }
        options.add(option); // add the option
        setOptions(options); // set the list of options to Parse
    }

    /*Méthod to remove a option from the list of meals*/
    public void removeOption(String optionID){
        List<String> options = getOptions();  //get the list of options from parse
        options.remove(optionID); // remove the string from the list
        setOptions(options); // set the list of options to Parse
    }

    /*id of the diet which this meal belongs*/
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

    /*user of the meal*/
    public void setUser(ParseUser user) {
        if (user != null) {
            put("user", user);
        }
    }

    public static ParseQuery<Meal> getQuery() {
        return ParseQuery.getQuery(Meal.class);
    }

    /*Compare to used to order meals by type*/
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
