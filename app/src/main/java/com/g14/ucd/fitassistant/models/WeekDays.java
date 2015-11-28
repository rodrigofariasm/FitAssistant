package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 16/10/2015.
 * Enum that maps the days of week with the values from parse
 */
public enum WeekDays {
    SUNDAY("Sun",1),
    MONDAY ("Mon",2),
    TUESDAY ("Tue",3),
    WEDNESDAY ("Wed",4),
    THURSDAY ("Thu",5),
    FRIDAY ("Fri",6),
    SATURDAY ("Sat",7);

    private String value;
    private int code;

    WeekDays(String value, int code){
        this.value = value;
        this.code = code;
    }

    /*code for the day in parse*/
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /*name of the day*/
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*Get the ENUM from the code*/
    public static WeekDays fromCode(int code){
        for(WeekDays wd : values()){ //iterates in the values from the enum
            if(wd.getCode() == code){ //if one value has the same code of the paramter
                return wd; // returns the enum
            }
        }
        return null;
    }

    public static WeekDays fromValue(String value){
        for(WeekDays wd : values()){
            if(wd.getValue() == value){
                return wd;
            }
        }
        return null;
    }
}
