package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natália on 17/10/2015.
 * This ParseObject represents a acitivity which the type is GYM.
 */
@ParseClassName("Gym")
public class Gym extends FitActivity {

    /*Name of the acitivity*/
    @Override
    public void setName(String name){
        put("name",name);
    }

    /*type of the acitivity, which is always gym*/
    @Override
    public void setType(int type) {
        put("type", ActivitiesTypeEnum.GYM.getCode());
    }

    /*List of exercises that the user must do in this gym activity*/
    public List<Exercise> getExercises() {
        return
                getList("exercises");
    }

    public void setExercises(List<Exercise> exercises) {
        put("exercises",exercises);
    }

    public void addExercise(Exercise exercise){
        List<Exercise> exercises = getExercises();
        if(exercises == null){
            setExercises(new ArrayList<Exercise>());
        }
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise){
        List<Exercise> exercises = getExercises();
        exercises.remove(exercise);
        setExercises(exercises);
    }
}
