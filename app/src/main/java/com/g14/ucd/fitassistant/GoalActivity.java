package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class GoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> goals, ParseException exception) {
                if (exception == null && goals.size() > 0) { // found goals
                    listGoals(goals);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                } else {
                    showButtons();
                }
            }
        });
    }

    private void listGoals(List<Goal> goals){

        ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_goal, // The name of the layout ID.
                R.id.list_item_name_goal, // The ID of the textview to populate.
                goals);

        ListView listView = (ListView) findViewById(R.id.listView_goals);
        listView.setAdapter(mAdapter);
    }

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

    public void addNewGoal(View v){
        addNewGoal();
    }

    private void addNewGoal(){
        Intent intent = new Intent(GoalActivity.this, NewGoalActivity.class);
        startActivity(intent);
    }

    public void update(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found diet
                    Intent intent = new Intent(GoalActivity.this, NewGoalActivity.class);
                    intent.putExtra("goal", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
                }
            }
        });


    }

    public void view(View v){
    }

    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Goal>() {
            @Override
            public void done(Goal goal, final ParseException exception) {
                if (exception == null) { // found goals
                    try {
                        goal.delete();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting goal" + e.getMessage());
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding goal with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }
}