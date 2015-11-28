package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 17/10/2015.
 * Enum that maps the days of week with the values from parse
 */
public enum ActivitiesTypeEnum {
    GYM("gym",1),
    SPORTS("sport",2),
    CLASSES("class",3);

    public String value;
    public int code;

    ActivitiesTypeEnum(String s, int c){
        this.value = s;
        this.code = c;
    }
    /*name of the type*/
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*code for the type in parse*/
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
