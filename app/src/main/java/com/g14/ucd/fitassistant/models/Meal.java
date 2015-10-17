package com.g14.ucd.fitassistant.models;

import java.util.ArrayList;

/**
 * Created by Leticia on 17/10/2015.
 */
public class Meal {

    public Enum type;
    public ArrayList<String> options;

    public Meal(Enum type, ArrayList<String> options) {
        this.type = type;
        this.options = options;
    }

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
