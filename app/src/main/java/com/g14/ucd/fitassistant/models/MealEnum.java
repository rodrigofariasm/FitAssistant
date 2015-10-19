package com.g14.ucd.fitassistant.models;

/**
 * Created by Leticia on 18/10/2015.
 */
public enum MealEnum {
    BREAKFAST("Breakfast",1),
    LUNCH("Lunch",2),
    DINNER("Dinner",3),
    SNACK("Snack",4);

    private String value;
    private int code;

    MealEnum(String value, int code){
        this.value = value;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
