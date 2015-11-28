package com.g14.ucd.fitassistant;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Activity for add a new event associated with some diet
* */
public class NewDietScheduletActivity extends AppCompatActivity {

    Spinner selectbox;
    List<Diet> dietsRetreived;
    List<Meal> mealsRetreived;
    DietEvent newDietEvent;
    ListView listView;
    CheckBox repeat;
    static EditText time;
    TextView meal;
    static Map<String,Date> times;
    List<Integer> weekdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_schedulet);
        listView = (ListView) findViewById(R.id.list_meals_schedule);
        time = (EditText) findViewById(R.id.meal_time);
        meal = (TextView) findViewById(R.id.item_name_meal);
        selectbox = (Spinner) findViewById(R.id.diets_spinner);
        repeat = (CheckBox) findViewById(R.id.repeat_diet);
        dietsRetreived = new ArrayList<Diet>();
        mealsRetreived = new ArrayList<Meal>();
        times = new HashMap<String,Date>();
        weekdays = new ArrayList<Integer>();

        if(!isUpdate()){
            newDietEvent = new DietEvent();
            findDiets(null);
        }
    }

    /*M�thod to find previous registred diets for the event. The user can only create a event if they have at least one diet registred
     */
    private void findDiets(final String dietUpdateId){

        ParseQuery<Diet> query = ParseQuery.getQuery("Diet"); // query for diet
        query.whereEqualTo("user", ParseUser.getCurrentUser()); // where the user for the diet is the current user
        query.findInBackground(new FindCallback<Diet>() { // find is another task asc
            @Override
            public void done(List<Diet> diets, ParseException exception) {
                if (exception == null && diets.size() > 0) { //if found diets
                    dietsRetreived = diets; // set the array of dietsRetrieveds
                    createSelectBox(diets, dietUpdateId); // and create the selectbox that show the names of the diets to user choose
                } else if (exception != null) { // if got an error
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                } else { // if found no diet
                    showButtonAddDiet(); // show button to redirect the user to de activity of creating diets
                }
            }
        });
    }

    private boolean isUpdate() {
        Bundle extras = getIntent().getExtras(); // get bundle
        if (extras != null) {
            final String objectId = extras.getString("dietEvent"); //get the dietEvent id of the event to be update that comes from the DietScheduleActivity if its update
            if (objectId != null) {// if found the id
                ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent"); //query for dietevent
                query.whereEqualTo("user", ParseUser.getCurrentUser()); // of this current user
                query.getInBackground(objectId, new GetCallback<DietEvent>() { // if the id of the event for update
                    @Override
                    public void done(DietEvent dietEvent, final ParseException exception) {
                        if (exception == null) { // if found the event
                            newDietEvent = dietEvent; //set the event found in the global object that saves/updates the event
                            fillFields(); //fill the fields of the page with the values from the event, so the user can update
                        } else if (exception != null) { //if found error
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
        return false;
    }

    /**Method to fill the fields of the page with the values from the event, so the user can update
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fillFields(){
        findDiets(newDietEvent.getDietId()); // find diets to assign to the event
        itemSelected(newDietEvent.getDietId()); //mark the diet of the event in update as selected in the spinner that shows the diets options
        times = newDietEvent.getTimes(); // get the meal times for the envent
        weekdays = newDietEvent.getWeekDays(); // get the days in the week that the event happens
        EditText editTextName = (EditText) this.findViewById(R.id.name_diet_schedule);// get the edittext in the xml for the name of the event
        editTextName.setText(newDietEvent.getName()); //set in the textview the name of the event for update
        TableRow weekdayButtons = (TableRow) findViewById(R.id.weekday_buttons);//get the view with the buttons to show the days in the week

        for(int i=0; i<weekdayButtons.getChildCount(); i++){ // iterates in the array of buttons
            TextView wd = (TextView) weekdayButtons.getChildAt(i);
            if(weekdays.contains(Integer.parseInt(wd.getTag().toString()))){ // if the button for the week has a value of one of the days in the week, which are numbers
                wd.setBackground(getDrawable(R.drawable.weekday_checkbox)); //set the background in the button that indicates that this day is selected
            }
        }
    }

    /**Creates the spinner with the option of diet registred
     * */
    public void createSelectBox(List<Diet> diets, String idDietUpdate){
        SpinnerAdapter<Diet> adapter =  new SpinnerAdapter(
                        this, // The current context (this activity)
                R.layout.spinner_item_diet, // The name of the layout ID.
                R.id.textView_spinner_name, // The ID of the textview to populate.
                diets);

        final Spinner spinnerDiet = (Spinner) findViewById(R.id.diets_spinner); // the spinner in the xml
        spinnerDiet.setAdapter(adapter); // set the adapter created in the spinner
        if(idDietUpdate != null){ //if the user is updating the diet, idDietUpdate is not null
            int positionSelected = adapter.getPosition(idDietUpdate); //get the position of the diet in the update event
            spinnerDiet.setSelection(positionSelected); //set the selection of the spinner according to that position
        }
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //set the method for onclick in the spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Diet diet = (Diet) spinnerDiet.getSelectedItem(); //get the diet selected
                itemSelected(diet.getObjectId()); //retrieve the meals for that diet selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**M�thod called when a diet is select in the spinner
     * */
    public void itemSelected(String dietId) {
        newDietEvent.setDietId(dietId); //set in the event the diet id
        if(dietId != null && findDietRetrieved(dietId) != null){ // if the id of the diet cliked is not empty and its on the list of diets retrieved for the user
            ParseQuery<Meal> query = ParseQuery.getQuery("Meal"); //create query for meals
            query.whereEqualTo("user", ParseUser.getCurrentUser()); // of that current user
            query.whereEqualTo("dietID", dietId); //which has the diet id
            try {
                mealsRetreived = query.find();
                generateListMeals(); // and generate the list of meals in the page
            } catch (ParseException e) {
                Log.d("FitAssistant", "Error searching meals" + e.getMessage());
            }
        }
    }


    /**M�thod that interates in the array of diets found search for one diet with the id @dietId
     * */
    public Diet findDietRetrieved(String dietId){
        for(Diet d : dietsRetreived){
            if(d.getObjectId().equals(dietId)){
                return d;
            }
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_diet_schedulet, menu);
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

    /**Generate the list of meals registred in this diet, so the user can set the time for each
     * */
    public void generateListMeals(){
        Collections.sort(mealsRetreived); //sort meals by the type. see compareTo for Meal
        ListMealScheduleAdapter mAdapter = new ListMealScheduleAdapter( //creates an adapter to list the meals type, and the textview for each time
                this,
                R.layout.list_meals_diet_schedule,
                R.id.item_name_meal, R.id.meal_time,
                mealsRetreived,newDietEvent);

        listView.setAdapter(mAdapter);//set the adapter

    }

    /**M�thod to show buttons for redirect the user to add diet activity
     * */
    public void showButtonAddDiet(){
        selectbox.setVisibility(View.INVISIBLE);
        Button addNewDiet = (Button) findViewById(R.id.button_add_diet);
    }

    /**Method to save the event created in Parse
     * */
    public void saveEvent(View v){
        final ProgressDialog dialog  = new ProgressDialog(this); //create dialog to show while the diet is been saved in parse
        dialog.setTitle(getString(R.string.progress_saving_event)); //set dialog message
        dialog.show(); //show the dialog

        EditText editTextName = (EditText) this.findViewById(R.id.name_diet_schedule); // get the view that contains the name of the envet
        newDietEvent.setName(editTextName.getText().toString()); //set event name from this the view
        newDietEvent.setTimes(times); //set time from the time array
        newDietEvent.setRepeat(repeat.isCheck()); //set if the event will be repeated every week or only this week
        newDietEvent.setNotification(CommonConstants.NOTIFICATION_ID); //set notification
        newDietEvent.setWeekDays(weekdays);//set array of days in the week which the event happens
        newDietEvent.setUser(ParseUser.getCurrentUser()); //se user for the event as the current
        newDietEvent.saveInBackground(new SaveCallback() { //saves in another task
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    dialog.dismiss(); //after finish, dismiss the dialog
                    Intent intent = new Intent(NewDietScheduletActivity.this, DietScheduleActivity.class);
                    createAlarms(); // create alarms
                    CommonConstants.NOTIFICATION_ID = CommonConstants.NOTIFICATION_ID +1;
                    startActivity(intent); //go back to the Diet Schedule list with this event created
                } else {
                    Log.d("FITASSISTANT", "Error saving day: " + e.getMessage());
                }
            }
        });
    }

    /**M�thod called when the user clicks in one of the buttons for days of week
     * */
    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    public void selectWeekDay(View view){
        TextView wd = (TextView) view; //get the button clicked
        int weekdayname = Integer.parseInt(wd.getTag().toString()); // get the number related to the day from the tag of button
        if(weekdays.contains(weekdayname)){ //if the number of the day clicked is already in the list means that it was selected and is beeing deselected
            weekdays.remove(new Integer(weekdayname)); //remove from the list with the selected ones
            view.setBackground(getDrawable(R.drawable.weekday_checkbox)); // set the background in the button that indicates that the button is not selected anymore
        } else { //if the number is not in the list, means that wasn't selected before, and is beeing select now
            weekdays.add(new Integer(weekdayname)); //add the number to the array of days selected
            view.setBackground(getDrawable(R.drawable.weekday_checkbox_clicked)); // set the background for the buttons that indicates that it is selected
        }
    }

    /**
    /**M�thod called when the user clicks in one of the edittext to set the time for the meal
     * */
    public void showTimePickerDialog(View v) {
        time = (EditText) v; //set the view that was clicked
        DialogFragment newFragment = new TimePickerFragment(); //create one fragment
        newFragment.show(getFragmentManager(), "timePicker"); //show the fragment
    }

    public static class TimePickerFragment extends android.app.DialogFragment
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
        /*M�thod called when the user selects a time in the timepicker
        */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.setText(hourOfDay + ":" + minute); //set in the editText the string for the time
            String key = time.getTag().toString(); //get the type of the meal of this time

            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
            try{
                Date timeDate = dateFormat.parse(time.getText().toString());
                times.put(key,timeDate); //add in the map of times for that event, the type and the date set noe
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**M�thod to create the notification of the event just created
     * */
    public void createAlarms(){
        ArrayList<AlarmManager> notifications = new ArrayList<>();
        for(Meal meal: mealsRetreived){ //iterate in the meals of the event. This method creates one notification for each meal
            for(Integer weekday : weekdays){ //and for each week day
                Intent mealIntent = new Intent(this, NotificationFitAssistant.class);
                Log.d(CommonConstants.DEBUG_TAG, MealEnum.fromCode(meal.getType()).getValue());
                mealIntent.putExtra(CommonConstants.EXTRA_MESSAGE, MealEnum.fromCode(meal.getType()).getValue());
                mealIntent.setAction(CommonConstants.ACTION_MEAL);
                PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), Application.notification_counter, mealIntent, PendingIntent.FLAG_ONE_SHOT);
                Application.notification_counter++;
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Date mealDate = times.get(""+MealEnum.fromCode(meal.getType()).getCode());
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
                notifications.add(MealEnum.fromCode(meal.getType()).getCode() - 1, alarmManager);

            }

        }
        Log.d(CommonConstants.DEBUG_TAG, ""+CommonConstants.NOTIFICATION_ID);
        Log.d(CommonConstants.DEBUG_TAG, notifications.toString());
        Application.notifications.put(CommonConstants.NOTIFICATION_ID, notifications);

    }


}
