package com.g14.ucd.fitassistant.models;

/**
 * Created by Leticia on 18/10/2015.
 */
public enum MealEnum {
    BREAKFAST("Breakfast",1),
    MORNING_SNAcK("Morning Snack",2),
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

    public static MealEnum fromCode(int code){
        for(MealEnum m : values()){
            if(m.getCode() == code){
                return m;
            }
        }
        return null;
    }
}
