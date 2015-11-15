package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natália on 17/10/2015.
 */
@ParseClassName("Gym")
public class Gym extends Activity {

    @Override
    public void setName(String name){
        put("name",ActivitiesTypeEnum.GYM.getValue());
    }

    @Override
    public void setType(int type) {
        put("type", ActivitiesTypeEnum.GYM.getCode());
    }

    public List<String> getExercises() {
        return getList("exercises");
    }

    public void setExercises(List<String> exercises) {
        put("exercises",exercises);
    }

    public void addExercise(String ID){
        List<String> exercises = getExercises();
        if(exercises == null){
            setExercises(new ArrayList<String>());
        }
        exercises.add(ID);
    }

    public void removeExercise(Integer ID){
        List<String> exercises = getExercises();
        exercises.remove(ID);
        setExercises(exercises);
    }
}
