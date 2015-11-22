package com.g14.ucd.fitassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewGoalActivity extends AppCompatActivity {

    private Goal newGoal;
    Spinner goalType;
    EditText actual;
    EditText desired;
    EditText start;
    EditText end;
    TextView actualUnit;
    TextView desiredUnit;
    ArrayAdapter<CharSequence> goalType_adapter;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        newGoal = new Goal();
        goalType = (Spinner) findViewById(R.id.goals_spinner);
        actual = (EditText) findViewById(R.id.editText_actual);
        desired = (EditText) findViewById(R.id.editText_desired);
        actualUnit = (TextView) findViewById(R.id.textView_actualUnit);
        desiredUnit = (TextView) findViewById(R.id.textView_desiredUnit);

        start = (EditText) findViewById(R.id.editText_start);
        start.setInputType(InputType.TYPE_NULL);
        start.requestFocus();

        end = (EditText) findViewById(R.id.editText_end);
        end.setInputType(InputType.TYPE_NULL);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        goalType_adapter = ArrayAdapter.createFromResource(this,
                R.array.goals_array, android.R.layout.simple_spinner_item);
        goalType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalType.setAdapter(goalType_adapter);

        if (!isUpdate()) {
            setDateTimeField();
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
            int pos = goalType_adapter.getPosition(newGoal.getType());
            goalType.setSelection(pos);
            actual.setText(Integer.toString(newGoal.getActual()));
            desired.setText(Integer.toString(newGoal.getDesired()));
            start.setText(dateFormatter.format(newGoal.getStart()));
            start.setHint(getString(R.string.start));
            start.setFocusable(true);
            end.setText(dateFormatter.format(newGoal.getEnd()));
            end.setHint(getString(R.string.end));
            end.setFocusable(true);
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
        newGoal.setType(goalType.getSelectedItem().toString());
        try {
            Date startDate = dateFormatter.parse(start.getText().toString());
            Date endDate = dateFormatter.parse(end.getText().toString());
            newGoal.setStart(startDate);
            newGoal.setEnd(endDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
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

    private void setDateTimeField(){
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                start.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                end.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public void showDate(View v) {
        if (v == start){
            startDatePickerDialog.show();
        }else{
            endDatePickerDialog.show();
        }
    }
}
