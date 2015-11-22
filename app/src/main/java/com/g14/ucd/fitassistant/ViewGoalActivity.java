package com.g14.ucd.fitassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewGoalActivity extends AppCompatActivity {

    private TextView goalTitle;
    private Goal goalView;
    private TextView dayNumber;
    private EditText current;
    SimpleDateFormat dateFormatter;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalTitle = (TextView) findViewById(R.id.textView_goalTitle);
        dayNumber = (TextView) findViewById(R.id.textView_dayNumber);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        Button registerHistory = (Button) findViewById(R.id.button_registerHistory);
        registerHistory.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        loadGoal();
    }

    public void showInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(ViewGoalActivity.this);
        View promptView = layoutInflater.inflate(R.layout.register_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewGoalActivity.this);
        alertDialogBuilder.setView(promptView);

        setValues(promptView);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveHistory();
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

    private void setValues(View view){
        TextView message = (TextView) view.findViewById(R.id.textView_current);
        TextView unit = (TextView) view.findViewById(R.id.textView_currentUnit);
        current = (EditText) view.findViewById(R.id.editText_current);
        String goalType = goalView.getType();
        switch (goalType){
            case ("Lose fat"):
                message.setText("Current fat percentage:");
                unit.setText("%");
                break;
            case ("Lose weight"):
                message.setText("Current weight:");
                unit.setText("kg");
                break;
            case ("Gain weight"):
                message.setText("Current weight:");
                unit.setText("kg");
                break;
        }
    }

    private void loadGoal(){
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
                        if (exception == null) { // found goal
                            goalView = goal;
                            loadTitle();
                            loadDayNumber();
                            loadGraph();
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
    }

    private void loadTitle(){
        if(goalView != null){
            String goalType = goalView.getString("type");
            long difference = Math.abs(goalView.getEnd().getTime() - goalView.getStart().getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String interval = Long.toString(differenceDates);
            String firstPart = null;
            switch (goalType){
                case("Lose fat"):
                    firstPart = "Lose " + Integer.toString(goalView.getInt("actual") - goalView.getInt("desired")) + "% of fat in ";
                    break;
                case("Lose weight"):
                    firstPart = "Lose " + Integer.toString(goalView.getInt("actual") - goalView.getInt("desired")) + "kg in ";
                    break;
                case("Gain weight"):
                    firstPart = "Gain " + Integer.toString(goalView.getInt("desired") - goalView.getInt("actual")) + "kg in ";
                    break;
                case("Follow diet"):
                    firstPart = "Follow the diet for ";
                    interval = Integer.toString(goalView.getInt("actual"));
                    break;
                case("Go to gym"):
                    firstPart = "Go to gym for ";
                    interval = Integer.toString(goalView.getInt("actual"));
                    break;
            }
            goalTitle.setText(firstPart + interval + " day(s)");
        }
    }

    private void loadDayNumber(){
        Date today = Calendar.getInstance().getTime();
        Date startDay = goalView.getStart();
        Date endDay = goalView.getEnd();
        String message = null;
        if(today.after(startDay) && today.before(endDay)){
            long difference = today.getTime() - startDay.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            message = "Day number: " + Long.toString(differenceDates+1);
        }else if (today.before(startDay)){
            long difference = startDay.getTime() - today.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            if(differenceDates >= 0 && differenceDates < 1){
                long differenceHours = difference / (60 * 60 * 1000);
                message = Long.toString(differenceHours) + " hours to start!";
            }else{
                message = Long.toString(differenceDates) + " days to start!";
            }
        }else if (today.after(endDay) && goalView.isActive()){
            message = "You finished your goal!";
        }
        dayNumber.setText(message);
    }


    private void saveHistory() {
        Map<String,Integer> record = goalView.getRecord();
        record.put(dateFormatter.format(Calendar.getInstance().getTime()),Integer.parseInt(current.getText().toString()));
        goalView.setRecord(record);
        goalView.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("FitAssistant", "Error: " + e.getMessage());
                }else{
                    graph.removeAllSeries();
                    loadGraph();
                }
            }
        });
    }

    public void loadGraph(){
        graph = (GraphView) findViewById(R.id.graph);
        Map<String,Integer> record = goalView.getRecord();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        int max = 0;
        for(String r : record.keySet()){
            try {
                series.appendData(new DataPoint(dateFormatter.parse(r).getTime(),record.get(r)),true,30);
                if (record.get(r) > max){
                    max = record.get(r);
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(max);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_goal, menu);
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
