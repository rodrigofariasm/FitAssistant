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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.g14.ucd.fitassistant.models.Day;
import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class NewDietScheduletActivity extends AppCompatActivity {

    TableLayout table;
    Spinner selectbox;
    List<Diet> dietsRetreived;
    List<Meal> mealsRetreived;

    Day day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet_schedulet);
        selectbox = (Spinner) findViewById(R.id.diets_spinner);
        day = new Day();
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
                R.id.diets_spinner, // The ID of the textview to populate.
                diets);

    }

    public void onItemSelected(View view){
        table.setVisibility(View.VISIBLE);
        String objectId = (String) view.getTag();

        if(objectId != null && findDietRetrieved(objectId) != null){
            ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("dietID", objectId);
            try {
                mealsRetreived = query.find();
                generateFields();
            } catch (ParseException e) {
                Log.d("FitAssistant", "Error deleting diet" + e.getMessage());
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

    public Meal findMealRetrieved(String mealId, int type){
        for(Meal m : mealsRetreived){
            if(mealId != null && m.getObjectId().equals(mealId)){
                return m;
            }
            if(mealId == null && m.getType() == type){
                return m;
            }
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_diet_schedulet, menu);
        table = (TableLayout) findViewById(R.id.table_schedule_diet);
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

    public void generateFields(){
        //bre
        findMealRetrieved(null, 1);

        //sna
        findMealRetrieved(null, 4);

        //lun
        findMealRetrieved(null, 2);

        //san
        findMealRetrieved(null,4);

        //din
        findMealRetrieved(null, 3);

        //sa
        findMealRetrieved(null,4);
    }

    public void showButtonAddDiet(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lienar_layout_diet_schedule);
        selectbox.setVisibility(View.INVISIBLE);
        Button addNewDiet = new Button(getBaseContext());
        addNewDiet.setText("Add diet");
        linearLayout.addView(addNewDiet);
    }
}
