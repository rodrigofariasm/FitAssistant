package com.g14.ucd.fitassistant.models;

/**
 * Created by Leticia on 18/10/2015.
 * Enum that maps the types of meal with the values from parse
 */
public enum MealEnum {
    BREAKFAST("Breakfast",1),
    MORNING_SNACK("Morning Snack",2),
    LUNCH("Lunch",3),
    AFTERNOON_SNACK("Afternoon Snack",4),
    DINNER("Dinner",5),
    NIGHT_SNACK("Night Snack",6);

    private String value;
    private int code;

    MealEnum(String value, int code){
        this.value = value;
        this.code = code;
    }

    /*code for the meal stored in parse*/
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /*name of the type of the meal*/
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*Get the ENUM from the code*/
    public static MealEnum fromCode(int code){
        for(MealEnum m : values()){ //iterates in the values from the enum
            if(m.getCode() == code){ //if one value has the same code of the paramter
                return m; //return the enum
            }
        }
        return null;
    }
}
