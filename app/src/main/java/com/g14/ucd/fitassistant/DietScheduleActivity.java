package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.Meal;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class reprensenting the schedule of a diet. The creation of a diet event.
 */
public class DietScheduleActivity extends AppCompatActivity {

    DietEvent newDietEvent;
    HashMap<DietEvent,ArrayList<Meal>> map;
    ArrayList<DietEvent> events;
    
    /**
	 * Method called everytime the activity is created.
	 * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);
        initialize();
    }

	/**
	 * method that initialize all the values
	 * */
    private void initialize(){
        newDietEvent = new DietEvent();
        events = new ArrayList<DietEvent>();
        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading));
        dialog.show();

        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvent, ParseException exception) {
                if (exception == null) { // found diets

                    if (dietEvent.size() == 0) {
                        showButtons();
                    } else {
                        events.addAll(dietEvent);
                        listDietEvents();
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
	 * method that list all the existing user's diet events
	 * */
    private void listDietEvents(){
       map = populateMap(events);

        ExpandableListDietEventAdapter mAdapter = new ExpandableListDietEventAdapter(
                this, // The current context (this activity)
                R.layout.list_item_diet, R.layout.list_item_meal, // The name of the layout ID.
                R.id.list_item_name_diet,R.id.button_update,R.id.button_delete,// The ID of the textview to populate.
                events, map);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView_dietSchedule);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

	/**
	 * method that returns the map containing the diet event with its
	 * respective meals
	 * */
    public HashMap<DietEvent,ArrayList<Meal>> populateMap(List<DietEvent> events){
        map= new HashMap<DietEvent,ArrayList<Meal>>();

        for(DietEvent de : events){
            ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("dietID", de.getDietId());
            try {
                ArrayList<Meal> meals = (ArrayList<Meal>) query.find();
                map.put(de,meals);
            } catch (ParseException e) {
                Log.d("FitAssistant", "Error" + e.getMessage());
            }
        }
        return map;
    }

	/**
	 * method that show the buttons and text message when the user don't
	 * have diet events
	 * */
    private void showButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_diet);
        addbutton.setVisibility(View.VISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_dietSchedule_message);
        noDietMessage.setVisibility(View.VISIBLE);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DietScheduleActivity.this, NewDietScheduletActivity.class);
                startActivity(intent);
            }
        });
    }

	/**
	 * method that hide the buttons and the "no events" text message when the user
	 * have diet events
	 * */
    private void hideButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_diet);
        addbutton.setVisibility(View.INVISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_dietSchedule_message);
        noDietMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet_schedule, menu);
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
        }else if (id == R.id.add_diet_event) {
            openNewDietScheduleActivity();
        }
        return super.onOptionsItemSelected(item);
    }

	/**
	 * Method called by the button "+event" to create a new diet event 
	 * */
    public void openNewDietScheduleActivity(){
        Intent intent = new Intent(DietScheduleActivity.this, NewDietScheduletActivity.class);
        startActivity(intent);

    }

	/**
	 * Method called by the update button. Open the NewDietScheduleActivity with
	 * the selected diet event in order to edit it.
	 * */
    public void update(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<DietEvent>() {
            @Override
            public void done(DietEvent diet, final ParseException exception) {
                if (exception == null) { // found diet
                    Intent intent = new Intent(DietScheduleActivity.this, NewDietScheduletActivity.class);
                    intent.putExtra("diet", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the delete button. Delete the selected diet event.
	 * */
    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<DietEvent>() {
            @Override
            public void done(DietEvent dietEvent, final ParseException exception) {
                if (exception == null) { // found diets
                    try {
                        dietEvent.delete();
                        initialize();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting diet event" + e.getMessage());
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet event with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }
}
