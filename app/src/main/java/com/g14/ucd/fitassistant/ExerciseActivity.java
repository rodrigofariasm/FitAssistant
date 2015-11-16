package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.ActivitiesTypeEnum;
import com.g14.ucd.fitassistant.models.Activity;
import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.Meal;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ParseQuery<Activity> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> activities, ParseException exception) {
                if (exception == null && activities.size() > 0) { // found diets
                    listActivities(activities);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                } else {
                    showButtons();
                }
            }
        });

    }

    private void listActivities(List<Activity> activities){

        ListAdapter mAdapter1 = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_diet, // The name of the layout ID.
                R.id.list_item_name_diet, // The ID of the textview to populate.
                activities);

        ListView listView = (ListView) findViewById(R.id.listView_exercises);
        listView.setAdapter(mAdapter1);

    }

    private void showButtons(){
        ImageButton addbutton = (ImageButton) findViewById(R.id.button_add_exercise);
        addbutton.setVisibility(View.VISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_exercise_message);
        noDietMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                //openSettings();
                return true;
            case R.id.logout_option:
                logout();
                return true;
            case R.id.diet_option:
                openDietActivity();
                return true;
            case R.id.exercise_option:
                openExerciseActivity();
                return true;
            case R.id.add_exercise:
                addNewExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(ExerciseActivity.this, DispatchActivity.class);
        startActivity(intent);
    }

    private void openDietActivity() {
        Intent intent = new Intent(ExerciseActivity.this, DietActivity.class);
        startActivity(intent);
    }

    private void openExerciseActivity() {
        Intent intent = new Intent(ExerciseActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void addNewExercise(View v){
        addNewExercise();
    }

    private void addNewExercise(){
        Intent intent = new Intent(ExerciseActivity.this, NewExerciseActivity.class);
        startActivity(intent);
    }

    public void update(View view){
        final String objectId = (String) view.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Activity> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Activity>() {
            @Override
            public void done(Activity activity, final ParseException exception) {
                if (exception == null) { // found diet
                    Intent intent = new Intent(ExerciseActivity.this, NewDietActivity.class);
                    intent.putExtra("activityId", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<Activity> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Activity>() {
            @Override
            public void done(Activity activity, final ParseException exception) {
                if (exception == null) { // found diets
                    if(activity.getType() == ActivitiesTypeEnum.GYM.getCode()){
                        ParseQuery<Exercise> query = ParseQuery.getQuery("Exercise");
                        query.whereEqualTo("user", ParseUser.getCurrentUser());
                        query.whereEqualTo("activityId", activity.getObjectId());
                        try {
                            List<Exercise> meals = query.find();
                            ParseObject.deleteAll(meals);
                            activity.delete();
                        } catch (ParseException e) {
                            Log.d("FitAssistant", "Error deleting gym activity" + e.getMessage());
                        }
                    } else {
                        try {
                            activity.delete();
                        } catch (ParseException e) {
                            Log.d("FitAssistant", "Error deleting other activity" + e.getMessage());
                        }
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

    public void view(View view){

    }


}
