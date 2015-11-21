package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.g14.ucd.fitassistant.models.ActivitiesTypeEnum;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.Other;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    ArrayList<FitActivity> exercises;
    HashMap<FitActivity, ArrayList<Exercise>> gymExercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exercises = new ArrayList<FitActivity>();
        gymExercises = new HashMap<FitActivity, ArrayList<Exercise>>();
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading_exercises));
        dialog.show();
        query.findInBackground(new FindCallback<Gym>() {

            @Override
            public void done(List<Gym> activities, ParseException exception) {
                if (exception == null ) {

                    if(activities.size() > 0)
                    exercises.addAll(activities);

                } else {
                    error_dialog.show();
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });

        dialog.show();
        ParseQuery<Other> queryOther = ParseQuery.getQuery("Other");
        queryOther.whereEqualTo("user", ParseUser.getCurrentUser());
        queryOther.findInBackground(new FindCallback<Other>() {
            @Override
            public void done(List<Other> activities, ParseException exception) {
                if (exception == null) {
                    if(!activities.isEmpty())
                        exercises.addAll(activities);
                        for(Other a: activities){
                            gymExercises.put(a,new ArrayList<Exercise>());
                        }

                } else {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                    error_dialog.show();
                }
            }
        });

        ParseQuery<Exercise> queryExercise = ParseQuery.getQuery("Exercise");
        queryExercise.whereEqualTo("user", ParseUser.getCurrentUser());
        queryExercise.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> activities, ParseException exception) {
                if (exception == null) {

                    for (Exercise e:activities
                         ) {
                        Log.d("FIT", e.getActivityID()+ " "+ e.getName() );
                        if(gymExercises.containsKey(e.getActivityID())){
                            ArrayList<Exercise> newAc = gymExercises.get(e.getActivityID());
                            newAc.add(e);
                            gymExercises.put(e.getActivityID(),
                                    newAc);

                        }else{
                            ArrayList<Exercise> newAc = new ArrayList<Exercise>();
                            newAc.add(e);
                            gymExercises.put(e.getActivityID(), newAc);
                        }
                        Log.d("FIT", ""+gymExercises.size());


                    }
                    dialog.dismiss();
                    if (!exercises.isEmpty()) {
                        hideButtons();
                        listExercises(exercises);
                        Log.d("FitAssistant", "ok");
                    }


                } else {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                    error_dialog.show();
                }
            }
        });



    }


    private void listExercises(List<FitActivity> activities){
        ExpandableListAdapter mAdapter = new ExpandableListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_exercise, // The name of the layout ID.
                R.id.list_item_name_exercise, R.id.button_update_exercise,R.id.button_delete_exercise,-1, // The ID of the textview to populate.
                activities, gymExercises, R.id.image_exercise_row);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.exampandable_listView_exercises);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }

    private void hideButtons(){
        ButtonFloat addButton = (ButtonFloat) findViewById(R.id.button_add_exercise);
        addButton.setVisibility(View.INVISIBLE);
        TextView noExerciseMessage = (TextView) findViewById(R.id.no_exercise_message);
        noExerciseMessage.setVisibility(View.INVISIBLE);
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
            case R.id.add_exercise:
                addNewExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void addNewExercise(View v){
        addNewExercise();
    }

    private void addNewExercise(){
        Intent intent = new Intent(ExerciseActivity.this, NewExerciseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void update(View view){
        final String objectId = (String) view.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Gym> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Gym>() {
            @Override
            public void done(Gym activity, final ParseException exception) {
                if (exception == null) { // found exercise
                    Intent intent = new Intent(ExerciseActivity.this, NewExerciseActivity.class);
                    intent.putExtra("activityId", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding exercise with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Gym>() {
            @Override
            public void done(Gym activity, final ParseException exception) {
                if (exception == null) { // found exercises
                    if(activity.getType() == ActivitiesTypeEnum.GYM.getCode()){
                        ParseQuery<Exercise> queryExercise = ParseQuery.getQuery("Exercise");
                        queryExercise.whereEqualTo("user", ParseUser.getCurrentUser());
                        queryExercise.whereEqualTo("activityId", activity.getObjectId());
                        try {
                            queryExercise.findInBackground(new FindCallback<Exercise>() {
                                @Override
                                public void done(List<Exercise> list, ParseException e) {
                                    for (Exercise e2: list
                                         ) {
                                        e2.deleteInBackground();
                                    }
                                }
                            });
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
                    Log.d("FitAssistant", "Error finding exercise with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

    public void view(View view){

    }


}
