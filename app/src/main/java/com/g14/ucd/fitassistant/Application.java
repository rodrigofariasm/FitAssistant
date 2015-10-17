package com.g14.ucd.fitassistant;

import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by rodrigofarias on 10/16/15.
 */
public class Application extends android.app.Application {

    public static final boolean APPDEBUG = false;

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;
    public static final String APPTAG = "FitAssistant";
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "IWa3fIqg1uxV59ZpqqqKwm5E7FUIWINYe6GYUGnF", "vdBuoTC4AJfADhwdxwwTapoUGoY84Idc4o9uF6FT");
        preferences = getSharedPreferences("com.parse.les142", Context.MODE_PRIVATE);
        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();
        //Security of data
        ParseACL defaultACL = new ParseACL();
        // If you would like objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation pi = ParseInstallation.getCurrentInstallation();



    }

    public static ConfigHelper getConfigHelper() {
        return configHelper;
    }

}
