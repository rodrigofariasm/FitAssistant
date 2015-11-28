package com.g14.ucd.fitassistant;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
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

import com.g14.ucd.fitassistant.models.exercise;
import com.g14.ucd.fitassistant.models.exerciseEvent;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.g14.ucd.fitassistant.models.Other;
import com.g14.ucd.fitassistant.models.WeekDays;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.CheckBox;
import com.parse.FindCallback;
import com.parse.GetCallback;
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

/**
 * Class reprensenting the schedule of a exercise. The creation of an exercise event.
 */
public class ExerciseScheduleActivity extends AppCompatActivity {

    private static EditText editText;
    private static Spinner spinnerFitActivity;
    private static CheckBox repeat;
    private Date time;
    private FitActivity fitActivity;
    private ExerciseEvent newEvent;
    private List<Integer> weekdays;

	/**
	 * Method called everytime the activity is created.
	 * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_schedule);
        initialize();
    }

	/**
	 * method that initialize all the values and makes the inicial query
	 * */
    private void initialize(){

        weekdays = new ArrayList<Integer>();
        newEvent = new ExerciseEvent();
        final ProgressDialog dialog  = new ProgressDialog(this);

        final com.gc.materialdesign.widgets.Dialog error_dialog = new com.gc.materialdesign.widgets.Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading));
        dialog.show();
        ParseQuery<ExerciseEvent> query = ParseQuery.getQuery("ExerciseEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ExerciseEvent>() {
            @Override
            public void done(List<ExerciseEvent> exerciseEvents, ParseException exception) {
                if (exception == null) { // found exercises
                    listActivities(exerciseEvents);
                    if (exerciseEvents.size() == 0) {
                        showButtons();
                    } else {
                        hideButtons();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                    error_dialog.show();
            }
                dialog.dismiss();
            }
        });
    }

	/**
	 * method that list all the existing user's exercise events
	 * */
    private void listActivities(List<ExerciseEvent> exerciseEvents){

        ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_exercise_event, // The name of the layout ID.
                R.id.list_item_name_exercise_event, 0, R.id.button_update_exercise_event,R.id.button_delete_exercise_event, -1,// The ID of the textview to populate.
                exerciseEvents);

        ListView listView = (ListView) findViewById(R.id.listView_exerciseSchedule);
        listView.setAdapter(mAdapter);

    }

	/**
	 * method that show the buttons and text message when the user don't
	 * have exercise events
	 * */
    private void showButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_exercise);
        addbutton.setVisibility(View.VISIBLE);
        TextView noexerciseMessage = (TextView) findViewById(R.id.no_exerciseSchedule_message);
        noexerciseMessage.setVisibility(View.VISIBLE);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

	/**
	 * method that hides the buttons and "no events" text message when the user
	 * have exercise events
	 * */
    private void hideButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_exercise);
        addbutton.setVisibility(View.INVISIBLE);
        TextView noexerciseMessage = (TextView) findViewById(R.id.no_exerciseSchedule_message);
        noexerciseMessage.setVisibility(View.INVISIBLE);
    }

	/**
	 * method that show the popup with the fields to create a new exercise
	 * */
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
            repeat = (CheckBox) promptView.findViewById(R.id.repeat);
            SpinnerAdapter<Exercise> adapter = new SpinnerAdapter(
                    ExerciseScheduleActivity.this, // The current context (this activity)
                    R.layout.spinner_item_exercise, // The name of the layout ID.
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

	/**
	 * Method called by the ok button on the pop up. Save the exercise
	 * that was created on the popup
	 * */
    private void save() {
        final ProgressDialog dialog  = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.progress_saving_event));
        dialog.show();
        fitActivity = (FitActivity) spinnerFitActivity.getSelectedItem();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try{
            time = dateFormat.parse(editText.getText().toString());
            newEvent.setRepeat(repeat.isCheck());
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
                if (e == null) {
                    createAlarms();
                    dialog.dismiss();
                    initialize();
                } else {
                    Log.d("FitAssistant", "Error: " + e.getMessage());

                }
            }
        });
    }
    
    /**
	 * Method called by the popup in order to select which days of the
	 * the event will happen
	 * */
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

	/**
	 * Method that says what will happen if select an item on the action bar
	 * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.add_exercise_event){
            openNewExerciseScheduleActivity();
        }
        return super.onOptionsItemSelected(item);
    }

	/**
	 * Method called by the button "+event" to create a new exercise event 
	 * */
    public void openNewExerciseScheduleActivity(){
        showInputDialog();
        return;
    }
	
	/**
	 * Method that shows the time picker dialog
	 * */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

	/**
	 * Class that represents the time picker dialog and its functionalities
	 * */
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

	/**
	 * Method that creat the alarm notification for the exercise event
	 * */
    public void createAlarms(){
        ArrayList<AlarmManager> notifications = new ArrayList<>();
        for(Integer weekday : weekdays){
            Intent exerciseIntent = new Intent(this, NotificationFitAssistant.class);
            exerciseIntent.putExtra(CommonConstants.EXTRA_MESSAGE, fitActivity.getName());
            exerciseIntent.setAction(CommonConstants.ACTION_EXERCISE);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), Application.notification_counter,
                    exerciseIntent, PendingIntent.FLAG_ONE_SHOT);
            Application.notification_counter++;
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Date mealDate = new Date(time.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, weekday);
            calendar.set(Calendar.HOUR_OF_DAY, mealDate.getHours());
            calendar.set(Calendar.MINUTE, mealDate.getMinutes());
            calendar.set(Calendar.SECOND, 00);
            long when = calendar.getTimeInMillis();
            Log.d(CommonConstants.DEBUG_TAG, calendar.getTime().toString());

            if(repeat.isCheck()){
                alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
            }else{
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when, 7*24*60*60*1000, pendingIntent);
            }
            notifications.add(alarmManager);

        }

    Log.d(CommonConstants.DEBUG_TAG, ""+CommonConstants.NOTIFICATION_ID);
    Log.d(CommonConstants.DEBUG_TAG, notifications.toString());
    Application.notifications.put(CommonConstants.NOTIFICATION_ID, notifications);

    }

	/**
	 * Method called by the delete button. Delete the selected exercise event.
	 * */
    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<ExerciseEvent> query = ParseQuery.getQuery("ExerciseEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<ExerciseEvent>() {
            @Override
            public void done(ExerciseEvent exeEvent, final ParseException exception) {
                if (exception == null) { // found exercises
                    try {
                        exeEvent.delete();
                        initialize();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting exercise event" + e.getMessage());
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding exercise event with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the update button. Open the NewexerciseScheduleActivity with
	 * the selected exercise event in order to edit it.
	 * */
    public void update(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<ExerciseEvent> query = ParseQuery.getQuery("ExerciseEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<ExerciseEvent>() {
            @Override
            public void done(ExerciseEvent exeEvent, final ParseException exception) {
                if (exception == null) { // found exercises
                    showInputDialog();
                    fillFields(exeEvent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding exercise event with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }
	
	/**
	 * Method that fill the popup fields with pre-existing information 
	 * if the user wants to update the exercise event
	 * */
    private void fillFields(ExerciseEvent event){
        spinnerFitActivity.setPrompt(event.getName());

        editText.setText(""+event.getTime().getHours()+":"+event.getTime().getMinutes());
        repeat.setChecked(event.getRepeat());

    }
}
