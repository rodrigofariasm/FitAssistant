package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 16/10/2015.
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

    public static WeekDays fromCode(int code){
        for(WeekDays wd : values()){
            if(wd.getCode() == code){
                return wd;
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
