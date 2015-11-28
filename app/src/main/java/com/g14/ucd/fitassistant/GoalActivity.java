package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.g14.ucd.fitassistant.models.Goal;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Class reprensenting the Goal Activity, the main screen of the Goal
 * that lists all the users goals.
 */

public class GoalActivity extends AppCompatActivity {

	/**
	 * Method called everytime the activity is created.
	 * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        initialize();
    }

	/**
	 * method that initialize all the values and makes the inicial query
	 * */
    private void initialize(){
        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading_goals));
        dialog.show();
        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> goals, ParseException exception) {
                if (exception == null) { // found goals
                    listGoals(goals);
                    dialog.dismiss();
                    if (goals.size() == 0) {
                        showButtons();
                    }
                }
                else if (exception != null) {
                    error_dialog.show();
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method to show the list of goals
	 * */
    private void listGoals(List<Goal> goals){

        ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_goal, // The name of the layout ID.
                R.id.list_item_name_goal, R.id.button_view, R.id.button_update,R.id.button_delete, R.id.switch_activate,// The ID of the textview to populate.
                goals);

        ListView listView = (ListView) findViewById(R.id.listView_goals);
        listView.setAdapter(mAdapter);
    }

	/**
	 * Method that shows the buttons and text message when there's no goal registered
	 * */
    private void showButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_goal);
        addbutton.setVisibility(View.VISIBLE);
        TextView noGoalMessage = (TextView) findViewById(R.id.no_goal_message);
        noGoalMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal, menu);
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
            case R.id.add_goal:
                addNewGoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	/**
	 * Method called by the button "+goal" to create a new goal 
	 * */
    public void addNewGoal(View v){
        addNewGoal();
    }

	/**
	 * Method called to create a new goal which opens a the NewgoalActivity
	 * */
    private void addNewGoal(){
        Intent intent = new Intent(GoalActivity.this, NewGoalActivity.class);
        startActivity(intent);
    }

	/**
	 * Method called by the update button. Open the NewGoalActivity with
	 * the selected goal in order to edit it.
	 * */
    public void update(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found goal
                    Intent intent = new Intent(GoalActivity.this, NewGoalActivity.class);
                    intent.putExtra("goal", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the delete button. Delete the selected goal.
	 * */
    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle("Deleting goal");
        dialog.show();
        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found goals
                    try {
                        goal.delete();
                        dialog.dismiss();
                        initialize();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting goal" + e.getMessage());
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the activate switch. Activate the selected goal.
	 * */
    public void activate(View v){
        final String objectId = (String) v.getTag();
        final Switch myswitch = (Switch) v;

        Log.d("TAG: objectId", objectId);

        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found goal
                    if(myswitch.isChecked()){
                        goal.setActive(true);
                    }else{
                        goal.setActive(false);
                    }
                    goal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.d("Fit assitant", " error saving goal: " + e.getMessage());
                            }
                        }
                    });
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the view button. Visualize the selected goal.
	 * */
    public void view(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading_viewGoal));
        dialog.show();
        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found goal
                    dialog.dismiss();
                    Intent intent = new Intent(GoalActivity.this, ViewGoalActivity.class);
                    intent.putExtra("goal", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    error_dialog.show();
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
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
