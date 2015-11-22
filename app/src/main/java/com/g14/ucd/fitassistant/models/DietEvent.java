package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Natália on 16/10/2015.
 */
@ParseClassName("DietEvent")
public class DietEvent extends ParseObject{

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name",name);
    }

    public String getDietId() {
        return getString("DietId");
    }

    public void setDietId(String dietId) {
        put("DietId",dietId);
    }

    public List<String> getActivitieIds() {
        return getList("activitieIds");
    }

    public void setActivitieIds(List<String> activitieIds) {
        put("activitieIds", activitieIds);
    }

    public List<Integer> getWeekDays(){
        return getList("weekDays");
    }

    public void setWeekDays( List<Integer> weekDays){
        put("weekDays",weekDays);
    }

    public void addWeekDay(int weekDay){
        List<Integer> weekDays = getWeekDays();
        if(weekDays == null){
            weekDays = new ArrayList<Integer>();
        }
        weekDays.add(weekDay);
        setWeekDays(weekDays);
    }

    public void removeOption(int weekDay){
        List<Integer> weekDays = getWeekDays();
        weekDays.remove(weekDay);
        setWeekDays(weekDays);
    }


    public void setTimes(Map<String,Date> times){
        put("times",times);
    }

    public Map<String,Date> getTimes(){
        return getMap("times");
    }

    public void addMealTime(Date date, int mealType){
        Map<String,Date> times = getTimes();

        if(times == null){
            times = new HashMap<String,Date>();
        }
        times.put(Integer.toString(mealType),date);
        setTimes(times);
    }


}
