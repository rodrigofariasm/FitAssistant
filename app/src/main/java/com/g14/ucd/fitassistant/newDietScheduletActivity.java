package com.g14.ucd.fitassistant;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewDietScheduletActivity extends AppCompatActivity {

    Spinner selectbox;
    List<Diet> dietsRetreived;
    List<Meal> mealsRetreived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_schedulet);
        selectbox = (Spinner) findViewById(R.id.diets_spinner);
        dietsRetreived = new ArrayList<Diet>();
        mealsRetreived = new ArrayList<Meal>();

        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Diet>() {
            @Override
            public void done(List<Diet> diets, ParseException exception) {
                if (exception == null && diets.size() > 0) { // found diets
                    dietsRetreived = diets;
                    createSelectBox(diets);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                } else {
                   showButtonAddDiet();
                }
            }
        });
    }

    public void createSelectBox(List<Diet> diets){
        SpinnerAdapter<Diet> adapter =  new SpinnerAdapter(
                        this, // The current context (this activity)
                R.layout.spinner_item_diet, // The name of the layout ID.
                R.id.textView_spinner_name, // The ID of the textview to populate.
                diets);

        Spinner spinnerDiet = (Spinner) findViewById(R.id.diets_spinner);
        spinnerDiet.setAdapter(adapter);
    }

    public void onItemSelected(View view){
        String objectId = (String) view.getTag();

        if(objectId != null && findDietRetrieved(objectId) != null){
            ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("dietID", objectId);
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
                R.id.item_name_diet, R.id.meal_time,
                mealsRetreived);

        ListView listView = (ListView) findViewById(R.id.list_meals_schedule);
        listView.setAdapter(mAdapter);
    }

    public void showButtonAddDiet(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lienar_layout_diet_schedule);
        selectbox.setVisibility(View.INVISIBLE);
        Button addNewDiet = new Button(getBaseContext());
        addNewDiet.setText("Add diet");
        linearLayout.addView(addNewDiet);
    }
}
