package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.gc.materialdesign.views.ButtonFlat;
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

/**
 * Class reprensenting the Exercise Activity, the main screen of the exercise
 * that lists all the users exercises.
 */
public class ExerciseActivity extends AppCompatActivity {
    ArrayList<FitActivity> exercises;
    HashMap<FitActivity, ArrayList<Exercise>> gymExercises;

	/**
	 * Method that is call everytime the activity is created
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
    }

	/**
	 * method that initialize all the values and makes the inicial query
	 * */
    private void initialize(){
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
                if (exception == null) {
                    if (activities.size() > 0)
                        exercises.addAll(activities);

                } else {
                    error_dialog.show();
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
        Exercise generic = new Exercise();
        final ArrayList<Exercise> exes = new ArrayList<Exercise>();
        exes.add(generic);

        ParseQuery<Other> queryOther = ParseQuery.getQuery("Other");
        queryOther.whereEqualTo("user", ParseUser.getCurrentUser());
        queryOther.findInBackground(new FindCallback<Other>() {
            @Override
            public void done(List<Other> activities, ParseException exception) {
            if (exception == null) {
                if(!activities.isEmpty())
                    exercises.addAll(activities);
                for(Other a: activities){
                    gymExercises.put(a,exes);
                }
                dialog.dismiss();

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
                }
                if (!exercises.isEmpty()) {
                    hideButtons();
                    listExercises(exercises);
                }
            } else {
                Log.d("FitAssistant", "Error: " + exception.getMessage());
                error_dialog.show();
            }
            }
        });
    }

	/**
	 * Method to show the list of exercises
	 * */
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
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

	/**
	 * Method that hides the buttons and text message when there's exercise registered
	 * */
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

	/**
	 * Method that says what will happen if select an item on the action bar
	 * */
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

	/**
	 * Method to called by the button to create a new exercise 
	 * */
    public void addNewExercise(View v){
        addNewExercise();
    }

	/**
	 * Method called to create a new exercise which opens a the NewExerciseActivity
	 * */
    private void addNewExercise(){
        Intent intent = new Intent(ExerciseActivity.this, NewExerciseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

	/**
	 * Method called by the update button. Open the NewExerciseActivity with
	 * the selected exercise in order to edit them.
	 * */
    public void update(View view){
        final String objectId = (String) view.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
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

	/**
	 * Method called by the delete button. Delete the selected exercise.
	 * */
    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle("Deleting exercise");
        dialog.show();
        final ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Gym>() {
            @Override
            public void done(Gym activity, final ParseException exception) {
                if (exception == null) { // found exercises
                    ParseQuery<Exercise> queryExercise = ParseQuery.getQuery("Exercise");
                    queryExercise.whereEqualTo("user", ParseUser.getCurrentUser());
                    queryExercise.whereEqualTo("activityID", activity);
                    try {
                        List<Exercise> exes = queryExercise.find();
                        ParseObject.deleteAll(exes);
                        activity.delete();
                        dialog.dismiss();
                        initialize();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting gym activity" + e.getMessage());
                    }
                } else {
                    Log.d("FitAssistant", "Error finding exercise with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method that is called everytime the activity is started.
	 * */
    @Override
    protected void onStart(){
        super.onStart();
        initialize();
    }
}
