package com.g14.ucd.fitassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewGoalActivity extends AppCompatActivity {

    private TextView goalTitle;
    private Goal goalView;
    private TextView dayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalTitle = (TextView) findViewById(R.id.textView_goalTitle);
        dayNumber = (TextView) findViewById(R.id.textView_dayNumber);

        loadGoal();
    }

    private void loadGoal(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String objectId = extras.getString("goal");
            Log.d("FitAssistant: ", "ObjectID: " + objectId);
            if (objectId != null) {
                ParseQuery<Goal> query = ParseQuery.getQuery("Goal");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(objectId, new GetCallback<Goal>() {
                    @Override
                    public void done(Goal goal, final ParseException exception) {
                        if (exception == null) { // found goal
                            goalView = goal;
                            loadTitle();
                            loadDayNumber();
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
    }

    private void loadTitle(){
        if(goalView != null){
            String goalType = goalView.getString("type");
            long difference = Math.abs(goalView.getEnd().getTime() - goalView.getStart().getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String interval = Long.toString(differenceDates);
            String firstPart = null;
            switch (goalType){
                case("Lose fat"):
                    firstPart = "Lose " + Integer.toString(goalView.getInt("actual") - goalView.getInt("desired")) + "% of fat in ";
                    break;
                case("Lose weight"):
                    firstPart = "Lose " + Integer.toString(goalView.getInt("actual") - goalView.getInt("desired")) + "kg in ";
                    break;
                case("Gain weight"):
                    firstPart = "Gain " + Integer.toString(goalView.getInt("desired") - goalView.getInt("actual")) + "kg in ";
                    break;
            }
            goalTitle.setText(firstPart + interval + " days");
        }
    }

    private void loadDayNumber(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
