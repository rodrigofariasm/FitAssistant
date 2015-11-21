package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natália on 17/10/2015.
 */
@ParseClassName("Gym")
public class Gym extends FitActivity {

    @Override
    public void setName(String name){
        put("name",name);
    }

    @Override
    public void setType(int type) {
        put("type", ActivitiesTypeEnum.GYM.getCode());
    }

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
