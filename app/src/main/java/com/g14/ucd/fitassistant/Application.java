package com.g14.ucd.fitassistant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Goal;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.facebook.FacebookSdk;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


/**
 * Created by rodrigofarias on 10/16/15.
 */
/**
 * Class reprensenting the Application, the Application establishes the connection with the Parse,
 * which is our server.
 */
public class Application extends android.app.Application {

    public static final boolean APPDEBUG = false;
    public static Integer notification_counter;
    private static SharedPreferences preferences;
    static Map<Integer, ArrayList<AlarmManager>> notifications;
    private static ConfigHelper configHelper;
    public static final String APPTAG = "FitAssistant";
    public static String singleton_date;


    @Override
    public void onCreate(){

        super.onCreate();
        notification_counter = Calendar.getInstance().getTime().getYear()*Calendar.getInstance().getTime().getDay();
        ParseObject.registerSubclass(Diet.class);
        ParseObject.registerSubclass(FitActivity.class);
        ParseObject.registerSubclass(Gym.class);
        ParseObject.registerSubclass(Other.class);
        ParseObject.registerSubclass(Exercise.class);
        ParseObject.registerSubclass(DietEvent.class);
        ParseObject.registerSubclass(Meal.class);
        ParseObject.registerSubclass(Goal.class);
        ParseObject.registerSubclass(ExerciseEvent.class);
        ParseObject.registerSubclass(Historic.class);
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

        if(ParseUser.getCurrentUser() != null){
            notifications = new HashMap<Integer, ArrayList<AlarmManager>>();
            if(singleton_date == null || singleton_date != findToday()){
                singleton_date = findToday();
            }

        }


    }

    public static ConfigHelper getConfigHelper() {
        return configHelper;
    }


    //Find the current day, necessary for showing the main screen and another features
    //of the app
    public String findToday(){
        Calendar day = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(day.getTime());
    }


}
