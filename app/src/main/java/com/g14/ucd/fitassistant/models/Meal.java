package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Leticia on 17/10/2015.
 */
@ParseClassName("Meal")
public class Meal extends ParseObject{

    public int type;
    public ArrayList<String> options;

    public Meal(int type, ArrayList<String> options) {
        this.type = type;
        this.options = options;
    }

    public Meal(){
        options = new ArrayList<String>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
