package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 17/10/2015.
 */
public class Exercise {

    public String name;
    public int sections;
    public int repetitions;

    public int getSections() {
        return sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
