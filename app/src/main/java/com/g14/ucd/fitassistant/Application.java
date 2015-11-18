package com.g14.ucd.fitassistant;

import android.content.Context;
import android.content.SharedPreferences;

import com.g14.ucd.fitassistant.models.Day;
import com.g14.ucd.fitassistant.models.Diet;
<<<<<<< HEAD
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
=======
import com.g14.ucd.fitassistant.models.Goal;
>>>>>>> 097de5aab3b00f9bc187c75130d5b91c9b123d8f
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.facebook.FacebookSdk;



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
        ParseObject.registerSubclass(Diet.class);
        ParseObject.registerSubclass(FitActivity.class);
        ParseObject.registerSubclass(Gym.class);
        ParseObject.registerSubclass(Other.class);
        ParseObject.registerSubclass(Exercise.class);
        ParseObject.registerSubclass(Day.class);
        ParseObject.registerSubclass(Meal.class);
        ParseObject.registerSubclass(Goal.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "IWa3fIqg1uxV59ZpqqqKwm5E7FUIWINYe6GYUGnF", "vdBuoTC4AJfADhwdxwwTapoUGoY84Idc4o9uF6FT");
        ParseFacebookUtils.initialize(this);

        preferences = getSharedPreferences("com.parse.les142", Context.MODE_PRIVATE);
        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();
        //Security of data
        ParseACL defaultACL = new ParseACL();
        // If you would like objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        FacebookSdk.sdkInitialize(getApplicationContext());


        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.


    }

    public static ConfigHelper getConfigHelper() {
        return configHelper;
    }

}
