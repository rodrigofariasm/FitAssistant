package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Natália on 16/10/2015.
 */
@ParseClassName("Day")
public class Day extends ParseObject{

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


    public void setTimes(Map<String,Integer> times){
        put("times",times);
    }

    public Map<String,Integer> getTimes(){
        return getMap("times");
    }

    public void addMealTime(String timeString, int mealType){
        Map<String,Integer> times = getTimes();
        int min = convertTimeStringToMin(timeString);
        if(times == null){
            times = new HashMap<String,Integer>();
        }
        times.put(Integer.toString(mealType),min);
        setTimes(times);
    }

    //Gets the minutes of the time for that meal specified in typeMeal and changes into string
    public String convertTimeHoursToMinutes(String typeMeal){
        int min = getTimes().get(typeMeal);
        int hours = min/60;
        int rest = min % 60;
        return hours + ":" + min;
    }

    public int convertTimeStringToMin(String time){
        int min = -1;
        int index = time.indexOf(":");
        if(index > -1){
            int h = Integer.parseInt(time.substring(0,index - 1));
            int m = Integer.parseInt(time.substring(index+1,time.length()));
            min = h*60 + m;
        }
        return min;
    }

}
