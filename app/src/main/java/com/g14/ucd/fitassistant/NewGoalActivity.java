package com.g14.ucd.fitassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewGoalActivity extends AppCompatActivity {

    private Goal newGoal;
    Spinner goalType;
    EditText actual;
    EditText desired;
    static EditText start;
    static EditText end;
    TextView actualUnit;
    TextView desiredUnit;
    static Date start_field;
    static Date end_field;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        if (!isUpdate()) {
            newGoal = new Goal();
            goalType = (Spinner) findViewById(R.id.goals_spinner);
            actual = (EditText) findViewById(R.id.editText_actual);
            desired = (EditText) findViewById(R.id.editText_desired);
            start = (EditText) findViewById(R.id.editText_start);
            end = (EditText) findViewById(R.id.editText_end);
            actualUnit = (TextView) findViewById(R.id.textView_actualUnit);
            desiredUnit = (TextView) findViewById(R.id.textView_desiredUnit);
            start_field = null;
            end_field = null;

            start.setInputType(InputType.TYPE_NULL);
            end.setInputType(InputType.TYPE_NULL);

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
            EditText actual_field = (EditText) findViewById(R.id.editText_actual);
            EditText desired_field = (EditText) findViewById(R.id.editText_desired);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.goals_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            goalType_field.setAdapter(adapter);

            int pos = adapter.getPosition(newGoal.getType());
            goalType_field.setSelection(pos);

            actual_field.setText(Integer.toString(newGoal.getActual()));
            desired_field.setText(Integer.toString(newGoal.getDesired()));

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
        newGoal.setStart(start_field);
        newGoal.setEnd(end_field);
        newGoal.setType(goalType.getSelectedItem().toString());
        newGoal.setActive(false);
        newGoal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(NewGoalActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("Fit assitant", " error saving goal: " + e.getMessage());
                }
            }
        });
    }

    public void showDatePickerDialogStart(View v) {
        DialogFragment newFragment = new DatePickerFragmentStart();
        newFragment.show(getSupportFragmentManager(), "datepicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }

    public static class DatePickerFragmentStart extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, month, day);
            start_field = newDate.getTime();
            start.setText((month+1) + "/" + day + "/" + year);
        }
    }

    public void showDatePickerDialogEnd(View v) {
        DialogFragment newFragment2 = new DatePickerFragmentEnd();
        newFragment2.show(getSupportFragmentManager(), "datepicker2");
    }

    public static class DatePickerFragmentEnd extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragmentEnd() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar newDate2 = Calendar.getInstance();
            newDate2.set(year, month, day);
            end_field = newDate2.getTime();
            end.setText((month+1) + "/" + day + "/" + year);
        }
    }
}
