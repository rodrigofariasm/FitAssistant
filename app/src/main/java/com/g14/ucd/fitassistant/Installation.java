package com.g14.ucd.fitassistant;

import android.app.AlarmManager;
import android.app.Notification;
import android.util.Log;
import com.parse.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodrigofarias on 10/17/15.
 */
public class Installation {
    public Installation(){

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    public void install(){
        ParseInstallation.getCurrentInstallation().put("user", ParseUser.getCurrentUser());
    }

}
