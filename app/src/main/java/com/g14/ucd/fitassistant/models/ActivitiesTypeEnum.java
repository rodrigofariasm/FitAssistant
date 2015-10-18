package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 17/10/2015.
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
