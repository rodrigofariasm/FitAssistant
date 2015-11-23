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

public class NewDietScheduletActivity extends AppCompatActivity {

    Spinner selectbox;
    List<Diet> dietsRetreived;
    List<Meal> mealsRetreived;
    DietEvent newDietEvent;
    ListView listView;
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
        dietsRetreived = new ArrayList<Diet>();
        mealsRetreived = new ArrayList<Meal>();
        times = new HashMap<String,Date>();
        weekdays = new ArrayList<Integer>();

        if(!isUpdate()){
            newDietEvent = new DietEvent();
            findDiets(null);
        }
    }

    private void findDiets(final String dietUpdateId){

        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Diet>() {
            @Override
            public void done(List<Diet> diets, ParseException exception) {
                if (exception == null && diets.size() > 0) { // found diets
                    dietsRetreived = diets;
                    createSelectBox(diets, dietUpdateId);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                } else {
                    showButtonAddDiet();
                }
            }
        });
    }

    private boolean isUpdate() {
        //Para recuperar os dados do Bundle em outra FitActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String objectId = extras.getString("dietEvent");
            Log.d("FitAssistant: ", "ObjectID: " + objectId);
            if (objectId != null) {
                ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(objectId, new GetCallback<DietEvent>() {
                    @Override
                    public void done(DietEvent dietEvent, final ParseException exception) {
                        if (exception == null) { // found diet
                            newDietEvent = dietEvent;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fillFields(){
        findDiets(newDietEvent.getDietId());
        itemSelected(newDietEvent.getDietId());
        times = newDietEvent.getTimes();
        weekdays = newDietEvent.getWeekDays();
        EditText editTextName = (EditText) this.findViewById(R.id.name_diet_schedule);
        editTextName.setText(newDietEvent.getName());
        TableRow weekdayButtons = (TableRow) findViewById(R.id.weekday_buttons);

        for(int i=0; i<weekdayButtons.getChildCount(); i++){
            TextView wd = (TextView) weekdayButtons.getChildAt(i);
            if(weekdays.contains(Integer.parseInt(wd.getTag().toString()))){
                wd.setBackground(getDrawable(R.drawable.weekday_checkbox));
            }
        }
    }

    public void createSelectBox(List<Diet> diets, String idDietUpdate){
        SpinnerAdapter<Diet> adapter =  new SpinnerAdapter(
                        this, // The current context (this activity)
                R.layout.spinner_item_diet, // The name of the layout ID.
                R.id.textView_spinner_name, // The ID of the textview to populate.
                diets);

        final Spinner spinnerDiet = (Spinner) findViewById(R.id.diets_spinner);
        spinnerDiet.setAdapter(adapter);
        if(idDietUpdate != null){
            int positionSelected = adapter.getPosition(idDietUpdate);
            spinnerDiet.setSelection(positionSelected);
        }
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Diet diet = (Diet) spinnerDiet.getSelectedItem();
                itemSelected(diet.getObjectId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void itemSelected(String dietId) {
        newDietEvent.setDietId(dietId);
        if(dietId != null && findDietRetrieved(dietId) != null){
            ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("dietID", dietId);
            try {
                mealsRetreived = query.find();
                generateListMeals();
            } catch (ParseException e) {
                Log.d("FitAssistant", "Error searching meals" + e.getMessage());
            }
        }
    }

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

    public void generateListMeals(){
        Collections.sort(mealsRetreived);

        ListMealScheduleAdapter mAdapter = new ListMealScheduleAdapter(
                this,
                R.layout.list_meals_diet_schedule,
                R.id.item_name_meal, R.id.meal_time,
                mealsRetreived,newDietEvent);

        listView.setAdapter(mAdapter);

    }

    public void showButtonAddDiet(){
        selectbox.setVisibility(View.INVISIBLE);
        Button addNewDiet = (Button) findViewById(R.id.button_add_diet);
    }


    public void saveEvent(View v){
        final ProgressDialog dialog  = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.progress_saving_event));
        dialog.show();

        EditText editTextName = (EditText) this.findViewById(R.id.name_diet_schedule);
        newDietEvent.setName(editTextName.getText().toString());
        newDietEvent.setTimes(times);
        newDietEvent.setNotification(CommonConstants.NOTIFICATION_ID);
        newDietEvent.setWeekDays(weekdays);
        newDietEvent.setUser(ParseUser.getCurrentUser());
        newDietEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    dialog.dismiss();
                    Intent intent = new Intent(NewDietScheduletActivity.this, DietScheduleActivity.class);
                    createAlarms();
                    CommonConstants.NOTIFICATION_ID = CommonConstants.NOTIFICATION_ID +1;
                    startActivity(intent);
                } else {
                    Log.d("FITASSISTANT", "Error saving day: " + e.getMessage());
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

    public void showTimePickerDialog(View v) {
        time = (EditText) v;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.setText(hourOfDay + ":" + minute);
            String key = time.getTag().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
            try{
                Date timeDate = dateFormat.parse(time.getText().toString());
                times.put(key,timeDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void createAlarms(){
        ArrayList<AlarmManager> notifications = new ArrayList<>();
        for(Meal meal: mealsRetreived){
            Intent mealIntent = new Intent(this, NotificationFitAssistant.class);
            Log.d(CommonConstants.DEBUG_TAG, MealEnum.fromCode(meal.getType()).getValue());
            mealIntent.putExtra(CommonConstants.EXTRA_MESSAGE, MealEnum.fromCode(meal.getType()).getValue());
            mealIntent.setAction(CommonConstants.ACTION_MEAL);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), notifications.size(), mealIntent, PendingIntent.FLAG_ONE_SHOT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Date mealDate = times.get(""+MealEnum.fromCode(meal.getType()).getCode());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, mealDate.getHours());
            calendar.set(Calendar.MINUTE, mealDate.getMinutes());
            calendar.set(Calendar.SECOND, 00);
            long when = calendar.getTimeInMillis();
            Log.d(CommonConstants.DEBUG_TAG, calendar.getTime().toString());
            alarmManager.set(AlarmManager.RTC, when, pendingIntent);
            notifications.add(MealEnum.fromCode(meal.getType()).getCode() - 1, alarmManager);

        }
        Log.d(CommonConstants.DEBUG_TAG, ""+CommonConstants.NOTIFICATION_ID);
        Log.d(CommonConstants.DEBUG_TAG, notifications.toString());
        Application.notifications.put(CommonConstants.NOTIFICATION_ID, notifications);

    }
}
