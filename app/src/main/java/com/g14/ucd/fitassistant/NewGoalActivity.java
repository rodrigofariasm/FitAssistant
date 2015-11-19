package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class NewGoalActivity extends AppCompatActivity {

    private Goal newGoal;
    Spinner goalType;
    Spinner time;
    EditText actual;
    EditText desired;
    EditText interval;
    TextView actualUnit;
    TextView desiredUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        if (!isUpdate()) {
            newGoal = new Goal();
            goalType = (Spinner) findViewById(R.id.goals_spinner);
            time = (Spinner) findViewById(R.id.time_spinner);
            actual = (EditText) findViewById(R.id.editText_actual);
            desired = (EditText) findViewById(R.id.editText_desired);
            interval = (EditText) findViewById(R.id.editText_interval);
            actualUnit = (TextView) findViewById(R.id.textView_actualUnit);
            desiredUnit = (TextView) findViewById(R.id.textView_desiredUnit);

            ArrayAdapter<CharSequence> goalType_adapter = ArrayAdapter.createFromResource(this,
                    R.array.goals_array, android.R.layout.simple_spinner_item);
            goalType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            goalType.setAdapter(goalType_adapter);

            goalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        actualUnit.setText("%");
                        desiredUnit.setText("%");
                    } else {
                        actualUnit.setText("kg");
                        desiredUnit.setText("kg");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    actualUnit.setText("%");
                    desiredUnit.setText("%");
                }
            });

            ArrayAdapter<CharSequence> time_adapter = ArrayAdapter.createFromResource(this,
                    R.array.time_array, android.R.layout.simple_spinner_item);
            time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            time.setAdapter(time_adapter);
        }
    }

    private boolean isUpdate() {
        //Para recuperar os dados do Bundle em outra FitActivity
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
                        if (exception == null) { // found diet
                            newGoal = goal;
                            fillFields();
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
        return false;
    }

    private void fillFields() {
        if (newGoal != null) {
            Spinner goalType_field = (Spinner) findViewById(R.id.goals_spinner);
            Spinner time_field = (Spinner) findViewById(R.id.time_spinner);
            EditText actual_field = (EditText) findViewById(R.id.editText_actual);
            EditText desired_field = (EditText) findViewById(R.id.editText_desired);
            EditText interval_field = (EditText) findViewById(R.id.editText_interval);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.goals_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            goalType_field.setAdapter(adapter);

            int pos = adapter.getPosition(newGoal.getType());
            goalType_field.setSelection(pos);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            time_field.setAdapter(adapter2);

            int pos2 = adapter2.getPosition(newGoal.getIntervalUnit());
            time_field.setSelection(pos2);

            actual_field.setText(Integer.toString(newGoal.getActual()));
            desired_field.setText(Integer.toString(newGoal.getDesired()));
            interval_field.setText(Integer.toString(newGoal.getInterval()));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_goal, menu);
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

    public void saveGoal(View view) {
        newGoal.setUser(ParseUser.getCurrentUser());
        newGoal.setActual(Integer.parseInt(actual.getText().toString()));
        newGoal.setDesired(Integer.parseInt(desired.getText().toString()));
        newGoal.setInterval(Integer.parseInt(interval.getText().toString()));
        newGoal.setIntervalUnit(time.getSelectedItem().toString());
        newGoal.setType(goalType.getSelectedItem().toString());
        newGoal.setActive(false);
        newGoal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(NewGoalActivity.this, GoalActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("Fit assitant", " error saving goal: " + e.getMessage());
                }
            }
        });
    }
}
