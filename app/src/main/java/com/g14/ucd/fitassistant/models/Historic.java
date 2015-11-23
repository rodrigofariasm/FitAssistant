package com.g14.ucd.fitassistant.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Natália on 17/10/2015.
 */
@ParseClassName("Historic")
public class Historic extends ParseObject {

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date",date);
    }

    public String getEventId() {
        return getString("eventId");
    }

    public void setEventId(String eventId) {
        put("eventId",eventId);
    }

}
