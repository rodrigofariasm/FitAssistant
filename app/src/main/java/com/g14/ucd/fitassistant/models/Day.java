package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Natália on 16/10/2015.
 */
@ParseClassName("Day")
public class Day extends ParseObject{

    public int getWeekday() {
        return getInt("weekday");
    }

    public void setWeekday(int weekday) {
        put("weekday",weekday);
    }

    public String getDietId() {
        return getString("DietId");
    }

    public void setDietId(String dietId) {
        put("DietId",dietId);
    }

    public int getMonthDay() {
        return getInt("monthDay");
    }

    public void setMonthDay(int monthDay) {
        put("monthDay",monthDay);
    }

    public List<String> getActivitieIds() {
        return getList("activitieIds");
    }

    public void setActivitieIds(List<String> activitieIds) {
        put("activitieIds", activitieIds);
    }

    public String getWeekDayName(int code){
        return WeekDays.fromCode(code).getValue();
    }


}
