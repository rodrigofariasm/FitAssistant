package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 16/10/2015.
 */
public enum WeekDays {
    SUNDAY("sunday",1),
    MONDAY ("Monday",2),
    TUESDAY ("Tuesday",3),
    WEDNESDAY ("Wednesday",4),
    THURSDAY ("Thursday",5),
    FRIDAY ("Friday",6),
    SATURDAY ("Saturday",7);

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
