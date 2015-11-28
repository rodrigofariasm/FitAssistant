package com.g14.ucd.fitassistant.models;

/**
 * Created by Leticia on 24/11/2015.
 * Enum that maps the types of the historic with the values from parse used in
 */
public enum HistoricType {
    DIET("Diet",0),
    EXERCISE("Exercises",1);

    private String value;
    private int code;

    HistoricType(String value, int code){
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

    public static HistoricType fromCode(int code){
        for(HistoricType m : values()){
            if(m.getCode() == code){
                return m;
            }
        }
        return null;
    }
}

