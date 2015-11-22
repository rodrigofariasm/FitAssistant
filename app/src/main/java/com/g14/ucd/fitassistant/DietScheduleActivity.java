package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DietScheduleActivity extends AppCompatActivity {

    DietEvent newDietEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);
        initialize();
    }

    private void initialize(){
        newDietEvent = new DietEvent();
        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvent, ParseException exception) {
                if (exception == null) { // found diets
                    listDietEvents(dietEvent);
                    if (dietEvent.size() == 0) {
                        showButtons();
                    } else {
                        hideButtons();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

    private void listDietEvents(List<DietEvent> events){

        ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_diet, // The name of the layout ID.
                R.id.list_item_name_diet,R.id.button_view, R.id.button_update,R.id.button_delete, -1,// The ID of the textview to populate.
                events);

        ListView listView = (ListView) findViewById(R.id.listView_dietSchedule);
        listView.setAdapter(mAdapter);

    }

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

    private void hideButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_schedule_exercise);
        addbutton.setVisibility(View.INVISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_exerciseSchedule_message);
        noDietMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet_schedule, menu);
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
}
