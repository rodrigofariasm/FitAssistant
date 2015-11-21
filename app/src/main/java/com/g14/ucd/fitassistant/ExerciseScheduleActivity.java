package com.g14.ucd.fitassistant;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Other;
import com.g14.ucd.fitassistant.models.WeekDays;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class ExerciseScheduleActivity extends AppCompatActivity {

    private static EditText editText;
    private static Spinner spinnerFitActivity;
    private ExerciseEvent newEvent;
    private List<Integer> weekdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_schedule);
        initialize();
    }

    private void initialize(){
        weekdays = new ArrayList<Integer>();
        newEvent = new ExerciseEvent();
        ParseQuery<ExerciseEvent> query = ParseQuery.getQuery("ExerciseEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ExerciseEvent>() {
            @Override
            public void done(List<ExerciseEvent> exerciseEvents, ParseException exception) {
                if (exception == null) { // found diets
                    listActivities(exerciseEvents);
                    if (exerciseEvents.size() == 0) {
                        showButtons();
                    }else{
                        hideButtons();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }


    private void listActivities(List<ExerciseEvent> exerciseEvents){

        ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_diet, // The name of the layout ID.
                R.id.list_item_name_diet,R.id.button_view, R.id.button_update,R.id.button_delete, -1,// The ID of the textview to populate.
                exerciseEvents);

        ListView listView = (ListView) findViewById(R.id.listView_exerciseSchedule);
        listView.setAdapter(mAdapter);

    }

    private void showButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_exercise);
        addbutton.setVisibility(View.VISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_exerciseSchedule_message);
        noDietMessage.setVisibility(View.VISIBLE);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    private void hideButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_exercise);
        addbutton.setVisibility(View.INVISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_exerciseSchedule_message);
        noDietMessage.setVisibility(View.INVISIBLE);
    }

    private void showInputDialog() {
        final List<FitActivity> exercises = new ArrayList<FitActivity>();
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Gym>() {

            @Override
            public void done(List<Gym> activities, ParseException exception) {
                if (exception == null) {
                    if (activities.size() > 0)
                        exercises.addAll(activities);
                } else {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });

        ParseQuery<Other> queryOther = ParseQuery.getQuery("Other");
        queryOther.whereEqualTo("user", ParseUser.getCurrentUser());
        try{
           List<Other> others = queryOther.find();
            exercises.addAll(others);
        } catch (ParseException e){
            Log.d("FITASSISTANT","Error: " + e.getMessage());
        }

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ExerciseScheduleActivity.this);
        View promptView = layoutInflater.inflate(R.layout.new_exercise_schedule_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExerciseScheduleActivity.this);
        alertDialogBuilder.setView(promptView);

        if (exercises.size() > 0) {
            spinnerFitActivity = (Spinner) promptView.findViewById(R.id.exerciseSpinner);
            editText = (EditText) promptView.findViewById(R.id.editText_timePicker);
            SpinnerAdapter<Exercise> adapter = new SpinnerAdapter(
                    ExerciseScheduleActivity.this, // The current context (this activity)
                    R.layout.spinner_item_diet, // The name of the layout ID.
                    R.id.textView_spinner_name, // The ID of the textview to populate.
                    exercises);

            spinnerFitActivity.setAdapter(adapter);
            spinnerFitActivity.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);


        } else {
            final TextView noExercise = (TextView) promptView.findViewById(R.id.text_view_noExercise);
            noExercise.setVisibility(View.VISIBLE);
            noExercise.setText("You don't have exercises to schedule. Create one!");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(exercises.size()>0){
                            save();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void save() {
        FitActivity fitActivity = (FitActivity) spinnerFitActivity.getSelectedItem();
        SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
        try{
            Date time = dateFormat.parse(editText.getText().toString());
            newEvent.setTime(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        newEvent.setWeekdays(weekdays);
        newEvent.setExerciseID(fitActivity.getObjectId());
        newEvent.setName(fitActivity.getName() + " - " + editText.getText().toString());
        newEvent.setUser(ParseUser.getCurrentUser());
        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.d("FitAssistant", "Error: " + e.getMessage());
                }else{
                    initialize();
                }
            }
        });
    }
    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    public void selectWeekDay(View view){
        TextView wd = (TextView) view;
        int weekdayname = Integer.parseInt(wd.getTag().toString());
        if(weekdays.contains(weekdayname)){
            weekdays.remove(new Integer(weekdayname));
            view.setBackground(getDrawable(R.drawable.weekday_checkbox));
        } else {
            weekdays.add(new Integer(weekdayname));
            view.setBackground(getDrawable(R.drawable.weekday_checkbox_clicked));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_schedule, menu);
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editText.setText(hourOfDay + ":" + minute);
        }
    }
}
