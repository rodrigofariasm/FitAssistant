package com.g14.ucd.fitassistant.models;

/**
 * Created by Natália on 17/10/2015.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FitActivity")
public class FitActivity extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public int getType() {
        return getInt("type");
    }

    public void setType(int type1){
        put("type",type1);
    }
    public void setUser(ParseUser value){
        if(value != null){
            put("user",value);
        }
    }
    public ParseUser getUser(){
        return getParseUser("user");
    }


}
