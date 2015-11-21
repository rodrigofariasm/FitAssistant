package com.g14.ucd.fitassistant;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Goal;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class ViewGoalActivity extends AppCompatActivity {

    private TextView goalTitle;
    private Goal goalView;
    private TextView dayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalTitle = (TextView) findViewById(R.id.textView_goalTitle);
        dayNumber = (TextView) findViewById(R.id.textView_dayNumber);

        final Button registerHistory = (Button) findViewById(R.id.button_registerHistory);
        registerHistory.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) ViewGoalActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.register_popup,null);
                final PopupWindow popupwindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                setValues(popupView);
                Button register = (Button)popupView.findViewById(R.id.register);
                register.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupwindow.dismiss();
                    }
                });
                popupwindow.showAsDropDown(registerHistory,50,-30);
            }
        });

        loadGoal();
    }

    private void setValues(View view){
        TextView message = (TextView)view.findViewById(R.id.textView_current);
        TextView unit = (TextView)view.findViewById(R.id.textView_currentUnit);
        EditText current = (EditText)view.findViewById(R.id.editText_current);
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
            }
            goalTitle.setText(firstPart + interval + " days");
        }
    }

    private void loadDayNumber(){
        Date today = Calendar.getInstance().getTime();
        Date startDay = goalView.getStart();
        Date endDay = goalView.getEnd();
        String message = null;
        if(today.after(startDay) && today.before(endDay)){
            long difference = Math.abs(today.getTime() - startDay.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            message = "Day number: " + Long.toString(differenceDates+1);
        }else if (today.before(startDay)){
            long difference = Math.abs(startDay.getTime() - today.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            if(differenceDates >= 0 || differenceDates < 1){
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

    public void registerHistory(View v){

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
