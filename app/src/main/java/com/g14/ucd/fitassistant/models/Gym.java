package com.g14.ucd.fitassistant.models;

import java.util.ArrayList;

/**
 * Created by Natália on 17/10/2015.
 */
public class Gym extends Activity {

    public final String NAME_GYM = "GYM";
    public ArrayList<Exercise> exercises;

    public Gym(ArrayList<Exercise> exercises){
        this.exercises.addAll(exercises);
    }

    @Override
    public String getName(){
        return NAME_GYM;
    }

    @Override
    public int getType() {
        return ActivitiesTypeEnum.GYM.getCode();
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
