package com.g14.ucd.fitassistant.models;

/**
 * Created by Leticia on 17/10/2015.
 */
public class Diet {

    public String name;
    public String description;

    public Diet(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
