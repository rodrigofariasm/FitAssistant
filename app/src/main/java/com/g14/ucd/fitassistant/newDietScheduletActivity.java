package com.g14.ucd.fitassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.g14.ucd.fitassistant.models.Day;
import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class NewDietScheduletActivity extends AppCompatActivity {

    Spinner selectbox;
    List<Diet> dietsRetreived;
    List<Meal> mealsRetreived;
    Day newDay;
    ListView listView;
    EditText time;
    TextView meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_schedulet);
        listView = (ListView) findViewById(R.id.list_meals_schedule);
        time = rl.findViewById(R.id.meal_time);
        meal = rl.findViewById(R.id.item_name_meal);
        selectbox = (Spinner) findViewById(R.id.diets_spinner);
        dietsRetreived = new ArrayList<Diet>();
        mealsRetreived = new ArrayList<Meal>();

        if(!isUpdate()){
            newDay = new Day();
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
            final String objectId = extras.getString("day");
            Log.d("FitAssistant: ", "ObjectID: " + objectId);
            if (objectId != null) {
                ParseQuery<Day> query = ParseQuery.getQuery("Day");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(objectId, new GetCallback<Day>() {
                    @Override
                    public void done(Day day, final ParseException exception) {
                        if (exception == null) { // found diet
                            newDay = day;
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
        return false;
    }

    public void fillFields(){
        findDiets(newDay.getDietId());
        onItemSelected(newDay.getDietId());
        for(int i =0; i < listView.getChildCount(); i++){
            RelativeLayout rl = (RelativeLayout) listView.getChildAt(i);
            String type = meal.getTag().toString();
            String timeString = newDay.convertTimeHoursToMinutes(type);
            time.setText(timeString);
        }
    }


    public void createSelectBox(List<Diet> diets, String idDietUpdate){
        SpinnerAdapter<Diet> adapter =  new SpinnerAdapter(
                        this, // The current context (this activity)
                R.layout.spinner_item_diet, // The name of the layout ID.
                R.id.textView_spinner_name, // The ID of the textview to populate.
                diets);

        Spinner spinnerDiet = (Spinner) findViewById(R.id.diets_spinner);
        spinnerDiet.setAdapter(adapter);
        if(idDietUpdate != null){
            int positionSelected = adapter.getPosition(idDietUpdate);
            spinnerDiet.setSelection(positionSelected);
        }
    }

    public void onItemSelected(View view){
        String objectId = (String) view.getTag();
        onItemSelected(objectId);
    }

    public void onItemSelected(String dietId){
        newDay.setDietId(dietId);
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
                mealsRetreived);

        listView.setAdapter(mAdapter);
    }

    public void showButtonAddDiet(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lienar_layout_diet_schedule);
        selectbox.setVisibility(View.INVISIBLE);
        Button addNewDiet = new Button(getBaseContext());
        addNewDiet.setText("Add diet");
        linearLayout.addView(addNewDiet);
    }



    public void saveDay(View v){
        int qtdMeals = listView.getChildCount();
        for(int count = 0; count<qtdMeals; count++){
            RelativeLayout rl = (RelativeLayout) listView.getChildAt(count);

            int typeCode = Integer.parseInt((String) meal.getTag());
            String mealTimeString = time.getText().toString();

            newDay.addMealTime(mealTimeString,typeCode);
        }

        newDay.setName("dia 1");
        newDay.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(NewDietScheduletActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("FITASSISTANT", "Error saving day: " + e.getMessage());
                }
            }
        });
    }



    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(v.getId());
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        static int viewId;

        public TimePickerFragment(int viewId){
            this.setViewId(viewId);
        }

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

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            EditText editTexttime = (EditText) getActivity().findViewById(viewId);
            editTexttime.setText(hourOfDay + ":" + minute);
        }

        public int getViewId() {
            return viewId;
        }

        public void setViewId(int viewId) {
            this.viewId = viewId;
        }
    }

}
